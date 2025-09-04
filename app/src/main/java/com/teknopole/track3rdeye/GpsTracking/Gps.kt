package com.teknopole.track3rdeye.GpsTracking

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.support.v4.app.ActivityCompat
import com.google.android.gms.location.*
import com.teknopole.track3rdeye.Utils.DeviceInfo.Companion.IsGpsEnabled


/**
 * Created by Md. Abdur Rouf on 6/24/2018.
 */
class Gps(private val context: Context,private val listener: GpsListener) {

    private val UPDATE_INTERVAL = (5 * 1000).toLong()  /* 5 secs */
    private val FASTEST_INTERVAL: Long = 2000 /* 2 sec */

    private var fusedLocationProviderClient:FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private var locationRequest: LocationRequest = LocationRequest()

    init {
        locationRequest.interval = UPDATE_INTERVAL
        locationRequest.fastestInterval = FASTEST_INTERVAL
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private val locationCallback= object: LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult?) {
            if (locationResult != null && locationResult.lastLocation !=null)
            {
                listener.OnLocationUpdates(locationResult.lastLocation)
            }
        }
    }

    fun SetUpdateInterval(sec:Int)
    {
        locationRequest.interval = (sec * 1000).toLong()
    }

    fun StartTracking()
    {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
           if (IsGpsEnabled(context)) {
               fusedLocationProviderClient.removeLocationUpdates(locationCallback)
               fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
               listener.OnTrackingStarted()
           }else
           {
               listener.GpsNotEnabled()
           }
        }else
        {
            // permission not granted
            listener.OnGpsPermissionDenied()
        }
    }
    fun StopTracking()
    {
       try {
           fusedLocationProviderClient.removeLocationUpdates(locationCallback)
           listener.OnTrackingStopped()
       }catch (e:Exception){}
    }

    interface GpsListener
    {
        fun OnGpsPermissionDenied()
        fun OnTrackingStarted()
        fun OnTrackingStopped()
        fun GpsNotEnabled()
        fun OnLocationUpdates(lastLocation: Location)
    }
}