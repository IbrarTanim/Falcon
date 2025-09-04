package com.teknopole.track3rdeye.Utils


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Point
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.os.BatteryManager
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.ContextCompat
import android.telephony.TelephonyManager
import android.view.WindowManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.teknopole.track3rdeye.BuildConfig

/**
 * Created by MD. ABDUR ROUF on 5/24/2018.
 */
class DeviceInfo {

    companion object {

        // get device info
        fun GetDeviceModel():String {
            return Build.MODEL ?: ""
        }
        fun GetAppVersion():String {
            return BuildConfig.VERSION_NAME
        }
        fun GetDeviceIMEI(context: Context): String {
            try {
                val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                return when {
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED -> "---------"
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> telephonyManager.imei
                    else -> telephonyManager.deviceId
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return ""
        }


        // get screen size
        fun GetScreenSize(context: Context): Point {
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val size = Point()
            windowManager.defaultDisplay.getSize(size)
            return size
        }
        fun displayWidth(context: Context): Int {
           return context.applicationContext.resources.displayMetrics.widthPixels
        }
        fun displayHeight(context: Context): Int {
            return context.applicationContext.resources.displayMetrics.heightPixels
        }




        fun GetBatteryChargeInPercent(context: Context):Int {
            try {
                val ifilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
                val batteryStatus = context.registerReceiver(null, ifilter)
                val level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                val scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                val batteryPct = level / scale.toFloat()
                return Math.round(batteryPct * 100)
            } catch (e: Exception) {
                e.printStackTrace()
                return 0
            }
        }
        fun IsWifiConnected(context: Context):Boolean {
            try {
                val mConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                return (mConnectivityManager.activeNetworkInfo != null
                        && mConnectivityManager.activeNetworkInfo.isConnected
                        && mConnectivityManager.activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI)
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }
        }
        fun IsGpsEnabled(context: Context):Boolean {
            try {
                val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                return if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                    true
                else
                {
                    val provider = Settings.Secure.getString(context.contentResolver, Settings.Secure.LOCATION_MODE)
                    (provider.contains("gps") || provider.contains("network"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }
        }
        fun IsInternetConnected(context: Context):Boolean {
            try {
                val mConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                return (mConnectivityManager.activeNetworkInfo != null
                        && mConnectivityManager.activeNetworkInfo.isAvailable
                        && mConnectivityManager.activeNetworkInfo.isConnected)
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }
        }



        // permissions
        fun IsAppInBatteryOptimizationMode(context: Context): Boolean {
            try {
                val pwrm = context.applicationContext.getSystemService(Context.POWER_SERVICE) as PowerManager
                val name = context.applicationContext.packageName
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    return !pwrm.isIgnoringBatteryOptimizations(name)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }
            return false
        }
        fun IsBackgroundDataUsesAllowed(context: Context):Boolean {
            try {
                val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    when (connMgr.restrictBackgroundStatus) {
                        RESTRICT_BACKGROUND_STATUS_ENABLED ->
                            // Background data usage and push notifications are blocked for this app
                            return false

                        RESTRICT_BACKGROUND_STATUS_WHITELISTED, RESTRICT_BACKGROUND_STATUS_DISABLED ->
                            // Data Saver is disabled or the app is whitelisted
                            return true
                    }
                }
                return true
            } catch (e: Exception) {
                e.printStackTrace()
                return true
            }
        }
        fun IsPlayServiceUpdated(context: Context): Boolean {
             return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS
        }
        fun IsNotificationEnabled(context: Context):Boolean {
            return NotificationManagerCompat.from(context).areNotificationsEnabled()
        }
        fun IsGpsPermissionGranted(context: Context):Boolean {
            return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        }
        fun IsReadPhoneStateAllowed(context: Context): Boolean {
            return (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)
        }

        fun IsCameraPermissionAllowed(context: Context): Boolean {
            return (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
        }

        fun IsAllPermissionsAreGranted(ctx:Context): Boolean {
            try {

                return if (!IsPlayServiceUpdated(ctx))
                    false
                else if (!IsGpsPermissionGranted(ctx))
                    false
                else if (IsAppInBatteryOptimizationMode(ctx))
                    false
                else if (!IsBackgroundDataUsesAllowed(ctx))
                    false
                else if (!IsNotificationEnabled(ctx))
                    false
                else if (!IsCameraPermissionAllowed(ctx))
                    false
                else IsReadPhoneStateAllowed(ctx)
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }
        }
    }
}