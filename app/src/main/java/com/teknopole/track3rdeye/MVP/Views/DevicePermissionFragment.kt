package com.teknopole.track3rdeye.MVP.Views


import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.teknopole.track3rdeye.App.app

import com.teknopole.track3rdeye.R
import com.teknopole.track3rdeye.Utils.DeviceInfo
import kotlinx.android.synthetic.main.fragment_device_permission.*

class DevicePermissionFragment : Fragment() {

    private var permissionIndex: Int = 0
    private lateinit var listener: ActionListener


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_device_permission, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btnGrantAccess.setOnClickListener {
            if (permissionIndex != 0) {
                TakeToPermissionSetting()
            }
        }
        CheckPermissionAndShowRightErrorMessage()
    }


    private fun CheckPermissionAndShowRightErrorMessage() {
        try {
            if (!DeviceInfo.IsPlayServiceUpdated(app.appContext)) {
                tvTitle.text = "Google Play Service Access"
                tvInfo.text = "Google Play Service version is not compatible with this app, please update !"
                ivIcon.setImageResource(R.drawable.img_help)
                permissionIndex = 1
            } else if (!DeviceInfo.IsGpsPermissionGranted(app.appContext)) {
                tvTitle.text = "GPS Service Access"
                tvInfo.text = "GPS Service Access needed to get your GPS location."
                ivIcon.setImageResource(R.drawable.img_satellite)
                permissionIndex = 2
            } else if (DeviceInfo.IsAppInBatteryOptimizationMode(app.appContext)) {
                tvTitle.text = "Battery Optimization Ignorance"
                tvInfo.text = "Ignore Battery Optimization for syncing and accessing the network."
                ivIcon.setImageResource(R.drawable.img_rocket)
                permissionIndex = 3
            } else if (!DeviceInfo.IsBackgroundDataUsesAllowed(app.appContext)) {
                tvTitle.text = "Background Data Usage Access"
                tvInfo.text = "Background data usage reflects the data used when the app is running in the background."
                ivIcon.setImageResource(R.drawable.img_safebox)
                permissionIndex = 4
            } else if (!DeviceInfo.IsNotificationEnabled(app.appContext)) {
                tvTitle.text = "Notification Access"
                tvInfo.text = "Notification access represents instant access to get notified with little to no energy."
                ivIcon.setImageResource(R.drawable.img_notification)
                permissionIndex = 5
            } else if (!DeviceInfo.IsReadPhoneStateAllowed(app.appContext)) {
                tvTitle.text = "Phone State Access"
                tvInfo.text = "It allows the application to access your phone features (e.g. IMEI number)."
                ivIcon.setImageResource(R.drawable.img_device_state)
                permissionIndex = 6
            } else if (!DeviceInfo.IsCameraPermissionAllowed(app.appContext)) {
                tvTitle.text = "Camera Access"
                tvInfo.text = "Photos/Images can be taken without zero hassle"
                ivIcon.setImageResource(R.drawable.img_photo_camera)
                permissionIndex = 7
            }
//            else if (!DeviceInfo.IsReadPhoneStateAllowed(context))
//            {
//                tvTitle.text="External Storage Access"
//                tvInfo.text="Permission needed to read device storage to get things work smoothly."
//                ivIcon.setImageResource(R.drawable.img_device_state)
//                permissionIndex=6
//            }
            else {
                // all permission granted
                listener.OnAllPermissionsGranted()
                //HomeActivity.ShowSuccessToast("You have successfully granted all required permissions.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun TakeToPermissionSetting() {
        when (permissionIndex) {
            1 -> RequestGooglePlayServiceUpdate(permissionIndex)
            2 -> RequestGpsPermission(permissionIndex)
            3 -> RequestIgnoreBatteryOptimizationPermission(permissionIndex)
            4 -> RequestBackgroundDataUsesPermission(permissionIndex)
            5 -> RequestNotificationsPermission(permissionIndex)
            6 -> RequestPhoneStatePermission(permissionIndex)
            7 -> RequestCameraPermission(permissionIndex)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == -1) {
            //HomeActivity.ShowSuccessToast("Permission granted")
        } else {
            if (requestCode == 5 && DeviceInfo.IsNotificationEnabled(app.appContext)) {
                //HomeActivity.ShowSuccessToast("Permission granted")
            } else
                HomeActivity.ShowWarningToast("Permission denied")
        }
        CheckPermissionAndShowRightErrorMessage()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults[0] == 0) {
            // HomeActivity.ShowSuccessToast("Permission granted")
        } else {
            HomeActivity.ShowWarningToast("Permission denied")
        }
        CheckPermissionAndShowRightErrorMessage()
    }


    private fun RequestGooglePlayServiceUpdate(requestCode: Int) {
        val LINK_TO_GOOGLE_PLAY_SERVICES = "play.google.com/store/apps/details?id=com.google.android.gms&hl=en"
        try {
            startActivityForResult(Intent(Intent.ACTION_VIEW, Uri.parse("market://$LINK_TO_GOOGLE_PLAY_SERVICES")), requestCode)
        } catch (anfe: android.content.ActivityNotFoundException) {
            startActivityForResult(Intent(Intent.ACTION_VIEW, Uri.parse("https://$LINK_TO_GOOGLE_PLAY_SERVICES")), requestCode)
        }

    }

    private fun RequestGpsPermission(requestCode: Int) {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), requestCode)
    }

    private fun RequestIgnoreBatteryOptimizationPermission(requestCode: Int) {
        try {
            val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
            intent.data = Uri.parse("package:" + app.appContext.packageName)
            startActivityForResult(intent, requestCode)

        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun RequestBackgroundDataUsesPermission(requestCode: Int) {
        try {
            val intent = Intent(Settings.ACTION_IGNORE_BACKGROUND_DATA_RESTRICTIONS_SETTINGS)
            intent.data = Uri.parse("package:" + app.appContext.packageName)
            startActivityForResult(intent, requestCode)

        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun RequestNotificationsPermission(requestCode: Int) {
        try {
            val intent = Intent()
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
                intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                intent.putExtra("android.provider.extra.APP_PACKAGE", app.appContext.packageName)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                intent.putExtra("app_package", app.appContext.packageName)
                intent.putExtra("app_uid", app.appContext.applicationInfo.uid)
            } else {
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                intent.addCategory(Intent.CATEGORY_DEFAULT)
                intent.data = Uri.parse("package:" + app.appContext.packageName)
            }

            startActivityForResult(intent, requestCode)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun RequestPhoneStatePermission(requestCode: Int) {
        try {
            requestPermissions(
                    arrayOf(Manifest.permission.READ_PHONE_STATE),
                    requestCode)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun RequestCameraPermission(requestCode: Int) {
        try {
            requestPermissions(
                    arrayOf(Manifest.permission.CAMERA),
                    requestCode)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    //============ Event ============
    fun SetEventListener(listener: ActionListener) {
        this.listener = listener
    }

    interface ActionListener {
        fun OnAllPermissionsGranted()
    }
}
