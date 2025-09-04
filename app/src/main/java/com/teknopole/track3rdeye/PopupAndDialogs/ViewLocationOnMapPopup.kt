package com.teknopole.track3rdeye.PopupAndDialogs

import android.Manifest
import android.animation.Animator
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.support.annotation.DrawableRes
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.*
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.GpsTracking.Gps
import com.teknopole.track3rdeye.MVP.Views.HomeActivity
import com.teknopole.track3rdeye.ObjectModels.TaskObject
import com.teknopole.track3rdeye.R
import com.teknopole.track3rdeye.Utils.Convert
import com.teknopole.track3rdeye.Utils.DeviceInfo


class ViewLocationOnMapPopup(private val task: TaskObject,private var instanceState: Bundle?) : OnMapReadyCallback {


    private var window: PopupWindow = PopupWindow(app.appContext)
    private val dimView:View
    private var dialogView: View
    private var progressBar: View
    private var tvAddress: TextView
    private var btnGetCurrentLocation: ImageView
    private var mapView: MapView
    private var currentPositionMarker: Marker?=null

    init {
        val v = LayoutInflater.from(app.appContext).inflate(R.layout.popup_view_location_on_map, null)
        dimView = v.findViewById(R.id.dimView)
        dialogView = v.findViewById(R.id.dialogView)
        progressBar = v.findViewById(R.id.progressBar)
        tvAddress = v.findViewById(R.id.tvLocationText)
        btnGetCurrentLocation = v.findViewById(R.id.btnGetCurrentLocation)

        window.contentView = v
        window.width = RelativeLayout.LayoutParams.MATCH_PARENT//DeviceInfo.displayWidth(app.appContext)
        window.height = RelativeLayout.LayoutParams.MATCH_PARENT
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.isOutsideTouchable = false
        window.isFocusable = false
        v.findViewById<TextView>(R.id.btnGotIt).setOnClickListener {Close()}
        btnGetCurrentLocation.setOnClickListener { getCurrentGpsLocation() }


        // Gets the MapView from the XML layout and creates it
        mapView = v.findViewById(R.id.mapView)
        mapView.onCreate(instanceState)
    }
    fun Close() {
        if (window.isShowing) {
            try {
                dimView.animate().alpha(0.0f)
                YoYo.with(Techniques.ZoomOut)
                        .withListener(object :Animator.AnimatorListener{
                            override fun onAnimationRepeat(animation: Animator?) {

                            }
                            override fun onAnimationCancel(animation: Animator?) {

                            }
                            override fun onAnimationStart(animation: Animator?) {

                            }

                            override fun onAnimationEnd(animation: Animator?) {
                                try {
                                    HomeActivity.SuspressBackPress=false
                                    mapView.onStop()
                                    mapView.onDestroy()
                                    window.dismiss()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        })
                        .duration(300)
                        .playOn(dialogView)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun Show(view: View) {
        if (!window.isShowing) {
            window.showAtLocation(view, Gravity.CENTER, 0, 0)
            startAnimation()
        }
    }
    private fun startAnimation() {
        Handler().postDelayed({
            try {
                HomeActivity.SuspressBackPress=true
                dimView.animate().alpha(0.8f)
                dialogView.visibility=View.VISIBLE
                YoYo.with(Techniques.ZoomIn)
                        .onEnd { initMap() }
                        .duration(500)
                        .interpolate(OvershootInterpolator(.9f))
                        .playOn(dialogView)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        },10)


    }


    private fun initMap() {
        try {
            mapView.getMapAsync(this)
            mapView.onResume()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    override fun onMapReady(map: GoogleMap?) {
      Handler().postDelayed({
          progressBar.visibility= View.GONE
          mapView.visibility= View.VISIBLE
          btnGetCurrentLocation.visibility = View.VISIBLE
          mapView.animate().setDuration(900).alpha(1f)
          UpdateMapData(map)
          tvAddress.text = Convert.GetGeoCodeAddress(task.taskLat,task.taskLng)
      },1000)
    }


    private fun UpdateMapData(map: GoogleMap?) {
        try {
            // Creating a marker
            val latLng = LatLng(task.taskLat, task.taskLng)

            // Placing a marker on the touched position
            map?.clear()
            currentPositionMarker = map?.addMarker(MarkerOptions().apply {
                title("Your Location")
                draggable(false)
                flat(true)
                position(LatLng(0.0,0.0))
                icon(Convert.vectorToBitmap(R.drawable.map_marker_current))
            })
            map?.addMarker( MarkerOptions().apply {
                position(latLng)
                title("Your Destination")
                draggable(false)
                flat(true)
                icon(Convert.vectorToBitmap(R.drawable.map_marker_destination))
            })?.showInfoWindow()
            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

        }catch (e: Exception) {
            e.printStackTrace()
        }
    }
//
//    private fun getCurrentLocation(){
//        try {// location details
//            val locationManager     =  app.appContext.getSystemService(LOCATION_SERVICE) as LocationManager
//            val criteria   =  Criteria()
//            val bestProvider = locationManager.getBestProvider(criteria, false)
//
//            if (ActivityCompat.checkSelfPermission(app.appContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
//                    ActivityCompat.checkSelfPermission(app.appContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
//                    DeviceInfo.IsGpsEnabled(app.appContext)) {
//                        val location   = locationManager.getLastKnownLocation(bestProvider)
//                        val posLatLon  =  LatLng(location.latitude, location.longitude)
//                        currentPositionMarker?.position = posLatLon
//                        currentPositionMarker?.showInfoWindow()
//                        mapView.getMapAsync {
//                        it?.animateCamera(CameraUpdateFactory.newLatLng(posLatLon))
//                }
//            }
//            else
//            {
//                btnGetCurrentLocation.visibility = View.GONE
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            btnGetCurrentLocation.visibility = View.GONE
//        }
//    }

    private fun getCurrentGpsLocation(){
        try {// location details
           var gps:Gps?=null

               Gps(app.appContext,object :Gps.GpsListener{
                override fun OnGpsPermissionDenied() {
                   T.show("Gps permission denied!")
                }
                override fun OnTrackingStarted() {

                }
                override fun OnTrackingStopped() {

                }
                override fun GpsNotEnabled() {
                    T.show("Gps is not enabled!")
                }

                override fun OnLocationUpdates(lastLocation: Location) {
                    try {
                        gps?.StopTracking()
                        val posLatLon  =  LatLng(lastLocation.latitude, lastLocation.longitude)
                        currentPositionMarker?.position = posLatLon
                        currentPositionMarker?.showInfoWindow()
                        mapView.getMapAsync {
                            it?.animateCamera(CameraUpdateFactory.newLatLng(posLatLon))
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        T.show("Gps location fetching error!")
                    }
                }
            }).apply {
                gps=this
                this.SetUpdateInterval(5)
                this.StartTracking()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            T.show("Gps location fetching error!")
        }
    }

}