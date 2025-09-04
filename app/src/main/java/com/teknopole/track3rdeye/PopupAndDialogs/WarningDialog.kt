package com.teknopole.track3rdeye.PopupAndDialogs

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.teknopole.track3rdeye.R
import com.teknopole.track3rdeye.Utils.DeviceInfo


/**
 * Created by Developer on 2/10/2018.
 */
class WarningDialog(private val context:Context) {
    private lateinit var alertDialog: AlertDialog


    private lateinit var view: View

    private var listener: ClickListener?=null

    init {
        try {
            alertDialog = AlertDialog.Builder(context).create()
             view = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.warning_dialog_layout, null)


            view.findViewById<TextView>(R.id.btnGotIt).setOnClickListener {
                Close()
            }

            alertDialog.setView(view)
            alertDialog.setCancelable(false)
            alertDialog.setCanceledOnTouchOutside(false)

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun Close() {
        if(alertDialog.isShowing) {
            listener?.OnClosed()
            alertDialog.dismiss()
        }
    }


    fun Show()
    {
        try {
            if(!alertDialog.isShowing)
            {
                alertDialog.show()

                alertDialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                alertDialog.window.setLayout((DeviceInfo.GetScreenSize(context).x * .90).toInt(), alertDialog.window.attributes.height)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun SetMessageText(msg:String): WarningDialog
    {
        view.findViewById<TextView>(R.id.tvMessage).text=msg
        return this
    }

    fun SetTitleText(titleText:String): WarningDialog
    {
        view.findViewById<TextView>(R.id.tvTitleText).text=titleText
        return this
    }

    fun SetButtonText(buttonText:String): WarningDialog
    {
        view.findViewById<TextView>(R.id.btnGotIt).text=buttonText
        return this
    }



    fun BackgroundDim(enable:Boolean): WarningDialog
    {
        var dim= if(enable) .4f else .07f
        alertDialog.window.setDimAmount(dim)
        return this
    }


    interface ClickListener
    {
        fun OnClosed()
    }
}