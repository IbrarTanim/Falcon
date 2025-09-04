package com.teknopole.track3rdeye.App

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.graphics.BitmapCompat
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.QueueProcessingType
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder
import com.nostra13.universalimageloader.core.download.BaseImageDownloader
import com.teknopole.track3rdeye.GpsTracking.TrackingConfig
import com.teknopole.track3rdeye.ObjectModels.Employee
import com.teknopole.track3rdeye.ObjectModels.LoginResponse
import com.teknopole.track3rdeye.R
import com.teknopole.track3rdeye.Utils.Convert
import com.teknopole.track3rdeye.Utils.LiveChange

class app:Application(){
    companion object {
        lateinit var appContext: Context
        //==============================\\

        // privacy policy settings
        fun SetPirvacyPolicyAccepted(accepted: Boolean) {
            try {
                appContext.getSharedPreferences("PrivacyPolicy", Context.MODE_PRIVATE).edit().apply {
                    putBoolean("accepted",accepted)
                }.apply()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        fun IsPirvacyPolicyAccepted(): Boolean {
            try {
                return appContext.getSharedPreferences("PrivacyPolicy", Context.MODE_PRIVATE).getBoolean("accepted",false)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }


        // FCM token
        fun SetFcmToken(token:String) {
            try {
                appContext.getSharedPreferences("FcmToken", Context.MODE_PRIVATE).edit().apply {
                    putString("token",token)
                }.apply()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        fun GetFcmToekn(): String {
            try {
                appContext.getSharedPreferences("FcmToken", Context.MODE_PRIVATE).apply {
                    return  getString("token","N")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return "N"
        }

        // User Session
        fun SetUserSession(emp: Employee) {
            try {
                appContext.getSharedPreferences("UserSession", Context.MODE_PRIVATE).edit().apply {
                    putInt("Id",emp.id)
                    putString("Username",emp.username)
                    putString("FirstName",emp.firstName)
                    putString("LastName",emp.lastName)
                    putString("Designation",emp.designation)
                    putString("Address",emp.address)
                    putString("BirthDate",emp.birthDate)
                    putString("Gender",emp.gender)
                    putString("Mobile",emp.mobile)
                    putString("Email",emp.email)
                    putString("Photo",emp.photo)
                    putInt("IsActive",emp.isActive)
                    putString("PermissionStatus",emp.permissionStatus)

                    putString("CompanyLogo",emp.companyLogo)
                    putString("CompanyName",emp.companyName)
                    putInt("CP_ProfileId",emp.cpProfileId) //company_id

                    putString("PrivacyPolicy",emp.privacyPolicy)


                    putInt("TrackingInterval",emp.trackingInterval)
                    putString("TrackingStartTime",emp.trackingStartTime)
                    putString("TrackingEndTime",emp.trackingEndTime)
                    putString("CanPause",emp.canPause)
                    putString("TrackingHistoryEnabled",emp.trackingHistoryEnabled)
                }.apply()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        fun GetUserSession(): Employee {
            val emp = Employee()
            try {
                appContext.getSharedPreferences("UserSession", Context.MODE_PRIVATE).apply {
                    emp.id =  getInt("Id",emp.id)
                    emp.username =   getString("Username",emp.username)
                    emp.firstName =   getString("FirstName",emp.firstName)
                    emp.lastName =   getString("LastName",emp.lastName)
                    emp.designation =   getString("Designation",emp.designation)
                    emp.address =   getString("Address",emp.address)
                    emp.birthDate =    getString("BirthDate",emp.birthDate)
                    emp.gender =   getString("Gender",emp.gender)
                    emp.mobile =   getString("Mobile",emp.mobile)
                    emp.email =   getString("Email",emp.email)
                    emp.photo =   getString("Photo",emp.photo)
                    emp.isActive =   getInt("IsActive",emp.isActive)
                    emp.permissionStatus =   getString("PermissionStatus",emp.permissionStatus)
                    emp.companyLogo =  getString("CompanyLogo",emp.companyLogo)
                    emp.companyName =   getString("CompanyName",emp.companyName)
                    emp.privacyPolicy =   getString("PrivacyPolicy","default-privacy-policy.pdf")
                    emp.cpProfileId =  getInt("CP_ProfileId",0)
                    emp.trackingInterval =  getInt("TrackingInterval",0)
                    emp.trackingStartTime =   getString("TrackingStartTime",emp.trackingStartTime)
                    emp.trackingEndTime =   getString("TrackingEndTime",emp.trackingEndTime)
                    emp.canPause =   getString("CanPause",emp.canPause)
                    emp.trackingHistoryEnabled =   getString("TrackingHistoryEnabled",emp.trackingHistoryEnabled)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return emp
        }
        fun UpdateEmployeeImage(imageName: String) {
            try {
                appContext.getSharedPreferences("UserSession", Context.MODE_PRIVATE).edit().apply {
                    putString("Photo",imageName)
                }.apply()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        fun ClearUserSession() {
            try {
                appContext.getSharedPreferences("UserSession", Context.MODE_PRIVATE).edit().clear().apply()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        // tracking pause or resume settings
        fun IsTrackingPaused(): Boolean {
            try {
               return appContext.getSharedPreferences("CanPauseSetting", Context.MODE_PRIVATE).getBoolean("PauseTracking",false)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return true
        }

        fun StoreImageToCache(imageName: String,bitmap: Bitmap) {
            try {
                appContext.getSharedPreferences("CacheImage", Context.MODE_PRIVATE).edit().apply {
                    putString(imageName,Convert.BitmapToBase64String(bitmap))
                }.apply()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        fun GetImageFromCache(imageName: String):Bitmap {
            return Convert.Base64StringToBitmap(appContext.getSharedPreferences("CacheImage", Context.MODE_PRIVATE).getString(imageName,""))
        }



        fun SetIsTrackingPaused(isOn: Boolean) {
            try {
                appContext.getSharedPreferences("CanPauseSetting", Context.MODE_PRIVATE).edit().apply {
                   putBoolean("PauseTracking",isOn)
                }.apply()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        // UniversalImageLoader config
        fun GetUILConfig():ImageLoaderConfiguration{
            return ImageLoaderConfiguration.Builder(app.appContext)
                    .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                    .diskCacheExtraOptions(480, 800, null)
                    .threadPoolSize(3) // default
                    .threadPriority(Thread.NORM_PRIORITY - 2) // default
                    .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                    .denyCacheImageMultipleSizesInMemory()
                    .memoryCache( LruMemoryCache(2 * 1024 * 1024))
                    .memoryCacheSize(2 * 1024 * 1024)
                    .memoryCacheSizePercentage(13) // default
                    .imageDownloader( BaseImageDownloader(app.appContext)) // default
                    .imageDecoder( BaseImageDecoder(false)) // default
                    .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                    .build()
        }


        // notification
        fun UpdateUnseenNotificationCount(unseenNotificationCount: Int) {
            try {
                appContext.getSharedPreferences("Notification", Context.MODE_PRIVATE).edit().apply {
                    putInt("unseenNotificationCount",unseenNotificationCount)
                }.apply()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            LiveChange.HomeFragment.liveOnNotificationCountChanged()
            LiveChange.NotificationsFragment.liveOnNotificationCountChanged()
        }
        fun GetUnseenNotificationCount():Int {
            return appContext.getSharedPreferences("Notification", Context.MODE_PRIVATE).getInt("unseenNotificationCount",0)
        }
        fun ShowCancelableNotification(title:String, msg: String) {
            val NID = 658
            val NCID=NID.toString()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val nm = app.appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                nm.createNotificationChannel(NotificationChannel(NCID, "NCID", NotificationManager.IMPORTANCE_DEFAULT))
            }

            val notification = NotificationCompat.Builder(app.appContext,NCID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker(msg)
                    .setContentTitle("3rd Eye")
                    .setContentText(msg)
                    .build()
            NotificationManagerCompat.from(app.appContext).notify(NID,notification)
        }
        fun ClearAllNotifications() {
            val nMgr = app.appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nMgr.cancelAll()
        }
        fun ClearNotification(notificationId:Int) {
            try {
                if (notificationId>0)
                    (app.appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).cancel(notificationId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        val isLoggedIn:Boolean get() = GetUserSession().id>0

        // update config
        fun UpdateUserSessionAndCongig(loginResponse: LoginResponse) {
            try {// set tracking days
                app.SetUserSession(loginResponse)
                if (loginResponse.trackingDays != null && !loginResponse.trackingDays.isEmpty()) {
                    TrackingConfig.UpdateTrackingDays(loginResponse.trackingDays)
                } else {
                    TrackingConfig.ClearTrackingDays()
                }

                // set tracking restriction date or date range
                if (loginResponse.trackingRestriction != null && !loginResponse.trackingRestriction.isEmpty()) {
                    TrackingConfig.UpdateTrackingRestrictions(loginResponse.trackingRestriction)
                } else {
                    TrackingConfig.ClearTrackingRestrictions()
                }


                // set privacy accepted no not
                if (!loginResponse.privacyPolicyAccepted.isNullOrEmpty() && loginResponse.privacyPolicyAccepted.equals("Yes", true)) {
                    app.SetPirvacyPolicyAccepted(true)
                } else {
                    app.SetPirvacyPolicyAccepted(false)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
    }
}