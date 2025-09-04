package com.teknopole.track3rdeye.PopupAndDialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.R
import com.teknopole.track3rdeye.Utils.DeviceInfo

/**
 * Created by MD. ABDUR ROUF on 5/24/2018.
 */
class MenuPopup(private val listener: MenuSelectListener) {
    private var window: PopupWindow = PopupWindow(app.appContext)

    init {
        val v = LayoutInflater.from(app.appContext).inflate(R.layout.popup_menu_user_profile, null)
        window.contentView = v
        window.width = ((DeviceInfo.displayWidth(app.appContext) * .60).toInt())
        window.height = RelativeLayout.LayoutParams.WRAP_CONTENT
        window.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT))
        window.isOutsideTouchable= true
        window.isFocusable=true

        v.findViewById<TextView>(R.id.btnEdit).setOnClickListener {
            listener.OnEditButtonClicked()
            Close()
        }
        v.findViewById<TextView>(R.id.btnChangePassword).setOnClickListener {
            listener.OnChangePasswordButtonClicked()
            Close()
        }
    }

    fun Close() {
        if (window.isShowing)
            window.dismiss()
    }

    fun Show(view: View)
    {
        if (!window.isShowing)
            window.showAsDropDown(view,-(view.width-45),-20,Gravity.END)
    }

    interface MenuSelectListener
    {
        fun OnEditButtonClicked()
        fun OnChangePasswordButtonClicked()
    }
}