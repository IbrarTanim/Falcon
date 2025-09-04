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
class ConfirmationDialog(private val context:Context,private val listener: ClickListener) {
    private lateinit var alertDialog: AlertDialog


    private lateinit var view: View

    init {
        try {
            alertDialog = AlertDialog.Builder(context).create()
             view = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.confirmation_dialog_layout, null)


            view.findViewById<TextView>(R.id.btnYes).setOnClickListener {
                listener.OnYesButtonClick()
                alertDialog.dismiss()
            }

            view.findViewById<TextView>(R.id.btnNo).setOnClickListener {
                listener.OnNoButtonClick()
                alertDialog.dismiss()
            }

            alertDialog.setView(view)
            alertDialog.setCancelable(false)
            alertDialog.setCanceledOnTouchOutside(false)

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    fun Show()
    {
        if(!alertDialog.isShowing)
        {
            alertDialog.show()

            alertDialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.window.setLayout((DeviceInfo.GetScreenSize(context).x * .85).toInt(), alertDialog.window.attributes.height)
        }
    }

    fun Close()
    {
        if (alertDialog.isShowing)
            alertDialog.dismiss()
    }
    fun SetConfirmationText(confirmationText:String): ConfirmationDialog
    {
        view.findViewById<TextView>(R.id.tvConfirmText).text=confirmationText
        return this
    }

    fun SetTitleText(titleText:String): ConfirmationDialog
    {
        view.findViewById<TextView>(R.id.tvTitleText).text=titleText
        return this
    }

    fun SetYesButtonText(yesButtonText:String): ConfirmationDialog
    {
        view.findViewById<TextView>(R.id.btnYes).text=yesButtonText
        return this
    }

    fun SetNoButtonText(noButtonText:String): ConfirmationDialog
    {
        view.findViewById<TextView>(R.id.btnNo).text=noButtonText
        return this
    }

    fun BackgroundDim(enable:Boolean): ConfirmationDialog
    {
        var dim= if(enable) .4f else .07f
        alertDialog.window.setDimAmount(dim)
        return this
    }

    interface ClickListener
    {
        fun OnYesButtonClick()
        fun OnNoButtonClick()
    }
}