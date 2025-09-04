package com.teknopole.track3rdeye.GpsTracking

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.teknopole.track3rdeye.App.app


class GpsTrackingServiceAutoStartStopReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Handler(Looper.getMainLooper()).post {
            if (app.isLoggedIn) {
                GpsTrackingService.StartOrStopUserLocationUpdateBasedOnConfiguration(context)
                GpsTrackingService.SetLocationServiceStartAndStopAlerm(context)
            }
        }
    }
}