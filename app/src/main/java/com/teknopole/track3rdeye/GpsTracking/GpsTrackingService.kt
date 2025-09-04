package com.teknopole.track3rdeye.GpsTracking

import android.app.AlarmManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.app.PendingIntent
import android.support.v4.app.NotificationCompat
import android.location.Location
import android.support.v4.app.NotificationManagerCompat
import com.teknopole.track3rdeye.R
import com.teknopole.track3rdeye.Utils.DeviceInfo.Companion.IsGpsEnabled
import java.util.*
import android.app.NotificationManager
import android.app.NotificationChannel
import android.location.Geocoder
import android.os.*
import android.util.Log
import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.App.appDatabase
import com.teknopole.track3rdeye.MVP.Views.HomeActivity
import com.teknopole.track3rdeye.ObjectModels.Tracking
import com.teknopole.track3rdeye.Utils.APIClient.ResponsePacket
import com.teknopole.track3rdeye.Utils.Convert
import com.teknopole.track3rdeye.Utils.DeviceInfo
import com.teknopole.track3rdeye.Utils.LiveChange
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit


/**
 * Created by Developer on 3/22/2018.
 */
class GpsTrackingService : Service(), Gps.GpsListener, TrackingDataSender.ActionListener {

    private val NOTIFICATION_ID = 577
    private var NOTIFICATION_CHANNEL_ID_SERVICE: String=NOTIFICATION_ID.toString()
    private lateinit var pendingIntent: PendingIntent

    private val timer = Handler()
    private var INTERVAL_TIME_IN_SEC: Long=30000

    private lateinit var gps: Gps
    private lateinit var wakeLock: PowerManager.WakeLock
    private lateinit var dataModel: TrackingDataSender

    private var OldLocation: Location?=null


    companion object {
        var IsServiceRunning = false

        val BROADCAST_SIGNOUT =  "signout"

        fun StartService(context: Context) {
            try {
                if (!IsServiceRunning)
                {
                    val serviceIntent = Intent(context, GpsTrackingService::class.java)
                    context.startService(serviceIntent)
                }
            } catch (e: Exception) {
                Log.e("tracking service",e.message)
            }
        }
        fun StopService(context: Context) {
            try {
                if (IsServiceRunning) {
                    val serviceIntent = Intent(context, GpsTrackingService::class.java)
                    context.stopService(serviceIntent)
                }
            } catch (e: Exception) {
                Log.e("tracking service",e.message)
            }
        }


        fun SignOutAndStopService(context: Context) {
            try {
                if (IsServiceRunning) {
                    val serviceIntent = Intent(context, GpsTrackingService::class.java)
                    serviceIntent.setAction(BROADCAST_SIGNOUT);
                    context.startService(serviceIntent)
                }
            } catch (e: Exception) {
                Log.e("tracking service",e.message)
            }
        }

        fun RestertService(context: Context) {
            try {
                SetLocationServiceStartAndStopAlerm(context)
                StopService(context)

                Handler(Looper.getMainLooper()).postDelayed({
                   StartOrStopUserLocationUpdateBasedOnConfiguration(context)
                },2000)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // start or stop background location update
        fun StartOrStopUserLocationUpdateBasedOnConfiguration(context: Context) {
            Thread {
                try {
                    if (app.GetUserSession().isActive == 1 && !TrackingConfig.IsTodayRestrictionDay() && TrackingConfig.IsTodayWorkingDay() && TrackingConfig.IsNowWorkingTime()) {
                        StartService(context)
                    }
                    else {
                        if(IsServiceRunning)
                        {
                            // Show Tracking terminated notification
                            app.ShowCancelableNotification("3rd Eye","Tracking terminated as per settings.")
                        }
                        StopService(context)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }.start()
        }
        fun SetLocationServiceStartAndStopAlerm(context: Context) {
            Thread{
                try {
                    val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    val intent = Intent(context, GpsTrackingServiceAutoStartStopReceiver::class.java)


                    val startIntent = PendingIntent.getBroadcast(context, 78, intent,PendingIntent.FLAG_UPDATE_CURRENT )
                    val stopIntent = PendingIntent.getBroadcast(context, 87, intent,PendingIntent.FLAG_UPDATE_CURRENT )


                    val startCalendar = TrackingConfig.GetTrackingStartTime()
                    val stopCalendar = TrackingConfig.GetTrackingEndTime()


                    // set next day if expired
                    if (startCalendar.before(Calendar.getInstance(Locale.getDefault()))) {
                        startCalendar.add(Calendar.DAY_OF_YEAR,1)
                    }
                    if (stopCalendar.before(Calendar.getInstance(Locale.getDefault()))) {
                        stopCalendar.add(Calendar.DAY_OF_YEAR,1)
                    }



                    // set stop alarm
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,startCalendar.timeInMillis, startIntent)
                    alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,stopCalendar.timeInMillis, stopIntent)

                       // alarmMgr.setAlarmClock(AlarmManager.AlarmClockInfo(startCalendar.timeInMillis,startIntent), startIntent)
                       // alarmMgr.setAlarmClock(AlarmManager.AlarmClockInfo(stopCalendar.timeInMillis,stopIntent), stopIntent)
                    }else
                    {
                        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, startCalendar.timeInMillis, AlarmManager.INTERVAL_DAY, startIntent)
                        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, stopCalendar.timeInMillis, AlarmManager.INTERVAL_DAY, stopIntent)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }.start()
        }
    }
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        IsServiceRunning = true
        Log.d("Start Command Intent ", intent?.getAction() ?: "")
        if (BROADCAST_SIGNOUT.equals(intent?.getAction() ?: "")) {
            onSignOut()
        } else {
            Init()
        }


        return START_STICKY
    }

    override fun onDestroy() {
        // Clear all notification
        val nMgr = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nMgr.cancel(NOTIFICATION_ID)
        gps.StopTracking()
        timer.removeCallbacksAndMessages(null)
        IsServiceRunning = false

        // live change
        LiveChange.HomeFragment.liveOnOnTrackingStopped()
        wakeLock.release()
        super.onDestroy()
    }



    // do stuff
    private fun Init() {
        try {
           wakeLock = (getSystemService(Context.POWER_SERVICE) as PowerManager).newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"lk")
           wakeLock.acquire(999999999999999999)


            // init interval time
            INTERVAL_TIME_IN_SEC = app.GetUserSession().trackingInterval.toLong() * 1000 // sec



            gps = Gps(applicationContext, this)
            dataModel = TrackingDataSender(this)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                nm.createNotificationChannel(NotificationChannel(NOTIFICATION_CHANNEL_ID_SERVICE, "Tracking Service", NotificationManager.IMPORTANCE_HIGH))
            }

            pendingIntent = PendingIntent.getActivity(this, 101,
                    Intent(app.appContext,HomeActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT)


            // make service foreground
            MakeServiceForground()

            // start tracking in initial point
            onTimeElapsed()

            // live change
            LiveChange.HomeFragment.liveOnOnTrackingStarted()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // on time to take location
    private fun onTimeElapsed() {
        timer.postDelayed({ onTimeElapsed() }, INTERVAL_TIME_IN_SEC)
        TakeUserData()
    }

    private fun onSignOut() {
        MakeTrackingObject(OldLocation, false)

        //logout delay to ensure sending of data
        Handler().postDelayed({
            LiveChange.HomeFragment.liveOnOnSignout()
        },500)

        Log.i("signout","Location : ${OldLocation?.latitude} ${OldLocation?.longitude}")
    }

    private var showingNotificationFor=""

    private fun TakeUserData() {
        Log.i("take", "user data")

        when{
            app.GetUserSession().id<=0 -> stopSelf()

            app.IsTrackingPaused() -> {

                MakeTrackingObject(null, true)

                if (showingNotificationFor != "paused") {
                    showingNotificationFor = "paused"
                    UpdateNotificationMessage("Tracking paused.")
                }
            }

            !IsGpsEnabled(app.appContext) -> {

                MakeTrackingObject(null, true)

                if (showingNotificationFor != "gps") {
                    showingNotificationFor = "gps"
                    UpdateNotificationMessage("Please enable GPS setting on this device.")
                }
            }
            else -> {
                gps.StartTracking()

                if (showingNotificationFor != "tracking") {
                    showingNotificationFor = "tracking"
                    UpdateNotificationMessage("Now you are being tracked.")
                }
            }
        }
    }


    // request foreground..
    private fun MakeServiceForground() {
        try {
            val notification = NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID_SERVICE)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(application.getString(R.string.app_name))
                    .setContentText("Tracking started as per settings.")
                    .setContentIntent(pendingIntent)
                    .build()
            startForeground(NOTIFICATION_ID, notification)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun UpdateNotificationMessage(message:String) {
        val notification = NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID_SERVICE)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(message)
                .setContentTitle(application.getString(R.string.app_name))
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .build()
        NotificationManagerCompat.from(applicationContext).notify(NOTIFICATION_ID,notification)
    }



    //    =============== GPS LISTENER ===============
    override fun OnGpsPermissionDenied() {
       // if (!AppConfig.GetGpsConfig(applicationContext).locUpdateBehaviour.equals("NO UPDATE",true) )
                UpdateNotificationMessage("Please give access Gps permission")
    }
    override fun OnTrackingStarted() {
        Log.d("Tracking","Tracking started")
    }
    override fun OnTrackingStopped() {
        Log.d("Tracking","Tracking stopped")
    }
    override fun GpsNotEnabled() {
        Log.d("Tracking","Gps not enabled")
        gps.StopTracking()

       // if (!AppConfig.GetGpsConfig(applicationContext).locUpdateBehaviour.equals("NO UPDATE",true) )
            UpdateNotificationMessage("Please enable gps")
    }
    override fun OnLocationUpdates(lastLocation: Location) {
        gps.StopTracking()
        MakeTrackingObject(lastLocation, true)
        Log.d("Tracking","Location found: ${lastLocation.latitude} ${lastLocation.longitude}")
    }
    // get geo code
    private fun GetGeoCodeAddress(latitude: Double,  longitude: Double): String {
        var address = "No address found."
        try {
            if (DeviceInfo.IsInternetConnected(applicationContext)) {
                val addr = Geocoder(applicationContext, Locale.getDefault()).getFromLocation(latitude, longitude, 1)
//                Log.d("addr", "" + addr)
                if (addr.size > 0) {
                    address = addr[0].getAddressLine(0)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return address
    }


    ////    ========== Server request===========
    private fun MakeTrackingObject(location: Location?,isLoggedIn: Boolean) {
        Thread {
            try {

                val tracking = Tracking()
                if(!app.IsTrackingPaused())
                {
                    if (location!=null)
                    {
                        // set updated location to tracking object
                        tracking.Lat = location.latitude
                        tracking.Lng = location.longitude
                        tracking.Accuracy=location.accuracy
                        tracking.Distance =location.distanceTo(location)
                        tracking.Speed= location.speed
                        tracking.Address=GetGeoCodeAddress(location.latitude, location.longitude)

                        // calculate distance and speed
                        if (OldLocation == null) {
                            tracking.Distance = 0f
                            tracking.Speed = 0f
                        } else {
                            tracking.Distance = Convert.GetLocationDistance(OldLocation!!, location)
                            tracking.Speed = Convert.GetSpeedPerSecond(tracking.Distance, Convert.GetDateDifference(Date(OldLocation!!.time), Date(location.time), TimeUnit.MILLISECONDS))
                        }


                        // update old location with new location
                        OldLocation = location
                    }
                }

                tracking.EmployeeId = app.GetUserSession().id
                tracking.Ttimes= SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(Date())
                tracking.IMEINumber=DeviceInfo.GetDeviceIMEI(app.appContext)
                tracking.GPSEnabled = if(DeviceInfo.IsGpsEnabled(app.appContext)) "Y" else "N"
                tracking.IsPaused = if(app.IsTrackingPaused()) "Y" else "N"
                tracking.InternetConnected = if(DeviceInfo.IsInternetConnected(app.appContext)) "Y" else "N"
                tracking.IsLoggedIn = if(isLoggedIn) "Y" else "N"
                tracking.BatteryPercentage = DeviceInfo.GetBatteryChargeInPercent(app.appContext)


                Log.d("insert track", tracking.toString())
                // store tracking data to database       ***
                appDatabase.Instance().trackingDao().InsertTracking(tracking)
            } catch (e: Exception) {
                e.printStackTrace()
            }


            // send tracking data
            SendTrackingsDataToRemoteServer()
        }.start()
    }
    private fun SendTrackingsDataToRemoteServer() {
        val trackings = appDatabase.Instance().trackingDao().GetAllTrackings()

        if (trackings.size > 1 && DeviceInfo.IsInternetConnected(applicationContext)) { //that means there is old data present in the database which needs to be send to the server
            for (track in trackings) {
                if(track.Address == "No address found.")
                    track.Address = GetGeoCodeAddress(track.Lat, track.Lng)
            }
        }


        if (trackings.isNotEmpty())
        {
            dataModel.RequestToPostTrackingData(trackings)
        }
    }

    override fun OnRequestToPostTrackingDataSuccess(response: ResponsePacket<Tracking>) {
        appDatabase.Instance().trackingDao().DeleteAllTrackings()
        Log.i("location update","success")
    }
}