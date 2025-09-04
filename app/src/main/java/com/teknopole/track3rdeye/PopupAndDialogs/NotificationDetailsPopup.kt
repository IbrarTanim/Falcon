package com.teknopole.track3rdeye.PopupAndDialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.MVP.Views.HomeActivity
import com.teknopole.track3rdeye.ObjectModels.NotificationObject
import com.teknopole.track3rdeye.R
import com.teknopole.track3rdeye.Utils.Convert

class NotificationDetailsPopup(private val notification: NotificationObject) {
    private var window: PopupWindow = PopupWindow(app.appContext)
    private val dimView:View
    private var dialogView: View

    init {
        val v = LayoutInflater.from(app.appContext).inflate(R.layout.popup_notification_details, null)
        dimView = v.findViewById(R.id.dimView)
        dialogView = v.findViewById(R.id.dialogView)

        window.contentView = v
        window.width = RelativeLayout.LayoutParams.MATCH_PARENT//DeviceInfo.displayWidth(app.appContext)
        window.height = RelativeLayout.LayoutParams.MATCH_PARENT
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.isOutsideTouchable = false
        window.isFocusable = false
        v.findViewById<TextView>(R.id.btnGotIt).setOnClickListener {Close()}

        v.findViewById<TextView>(R.id.tvTitleText).text = notification.title
        v.findViewById<TextView>(R.id.tvMessage).text = notification.description
        v.findViewById<TextView>(R.id.tvTime).text = Convert.FormatDateTime(notification.generatedOn,"dd MMM, yyyy")
    }

    fun Close() {
        if (window.isShowing) {
            try {
                dimView.animate().alpha(0.0f)
                YoYo.with(Techniques.ZoomOut)
                        .onEnd {
                            try {
                                HomeActivity.SuspressBackPress=false
                                window.dismiss()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        .duration(300)
                        .playOn(dialogView)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun Show(view: View) {
        if (!window.isShowing)
            window.showAtLocation(view,Gravity.CENTER,0,0)

        startAnimation()
    }

    private fun startAnimation() {
        Handler().postDelayed({
            try {
                HomeActivity.SuspressBackPress=true
                dimView.animate().alpha(0.8f)
                dialogView.visibility=View.VISIBLE
                YoYo.with(Techniques.ZoomIn)
                        .duration(500)
                        .interpolate(OvershootInterpolator(.9f))
                        .playOn(dialogView)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        },10)
    }
}