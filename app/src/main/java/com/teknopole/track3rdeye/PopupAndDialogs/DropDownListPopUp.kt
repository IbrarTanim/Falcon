package com.teknopole.track3rdeye.PopupAndDialogs

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.teknopole.track3rdeye.R
import com.teknopole.track3rdeye.Utils.Convert
import com.teknopole.track3rdeye.Utils.DeviceInfo

/**
 * Created by Developer on 2/10/2018.
 */
class DropDownListPopUp(private val context:Context, private val actionList:ArrayList<String>, private val listener: ItemSelectListener) {

    private var popupWindow: PopupWindow = PopupWindow(context)
    private var window_layout: LinearLayout = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.dropdown_list_popup_layout, null) as LinearLayout

    init {

        popupWindow.contentView = window_layout
        popupWindow.width = ((DeviceInfo.GetScreenSize(context).x * .70).toInt())
        popupWindow.height = RelativeLayout.LayoutParams.WRAP_CONTENT
        popupWindow.setBackgroundDrawable( ColorDrawable(Color.WHITE))
        popupWindow.elevation=8f
        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable=true


        // add button to ui
        for(action in actionList)
        {
            val button = Button(context)
            button.text=action
            button.setBackgroundResource(R.drawable.bg_dropdown_list_button)
            button.setOnClickListener {
                listener.OnItemSelected(action)
                popupWindow.dismiss()
            }
            button.gravity= Gravity.CENTER_VERTICAL
            button.setAllCaps(false)
            button.setTextColor(ContextCompat.getColor(context,R.color.colorTextBlack))
            button.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(R.dimen.textSmall))
            button.setPadding(Convert.DpToPixel(10),Convert.DpToPixel(5),Convert.DpToPixel(5),Convert.DpToPixel(5))
            window_layout.addView(button)
        }
    }


    // show Popup
    fun Show(view:View)
    {
        if(!popupWindow.isShowing) {
            popupWindow.width = view.width
            popupWindow.showAsDropDown(view)
        }
    }

    fun Close() {
        if(popupWindow.isShowing)
            popupWindow.dismiss()
    }


    // define click listener
    interface ItemSelectListener
    {
        fun OnItemSelected(actionText:String)
    }
}