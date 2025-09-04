package com.teknopole.track3rdeye.PopupAndDialogs

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import com.teknopole.track3rdeye.R
import com.teknopole.track3rdeye.Utils.Convert
import com.teknopole.track3rdeye.Utils.DeviceInfo
import java.util.*


/**
 * Created by Developer on 2/10/2018.
 */
class DatePickerDialog(private val context:Context,private val d:Int,private val m:Int, private val y:Int, private val listener: ClickListener) {
    private lateinit var alertDialog: AlertDialog


    private lateinit var view: View

    init {
        try {
            alertDialog = AlertDialog.Builder(context).create()
             view = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.date_picker_dialog_layout, null)


            view.findViewById<Button>(R.id.btnSubmit).setOnClickListener {
                val picker = view.findViewById<DatePicker>(R.id.datePicker)
                listener.onDateSelected( Convert.IntDateToStringDate(picker.year,picker.month+1, picker.dayOfMonth,"dd/MM/yyyy"))
                alertDialog.dismiss()
            }


            //view.findViewById<TextView>(R.id.tvDate).text= Convert.IntDateToStringDate(year,month,day,"dd MMM, yyyy")
           view.findViewById<DatePicker>(R.id.datePicker)
                   .apply {
                       val c = Calendar.getInstance()
                       maxDate = c.timeInMillis
                       c.add(Calendar.YEAR, -80)
                       minDate = c.timeInMillis
                   }
                   .init(y,m-1,d
                   ) { p, y, m, d->
                       view.findViewById<TextView>(R.id.tvDate).text= Convert.IntDateToStringDate(y,m+1,d,"dd MMM, yyyy")
                   }

            alertDialog.setView(view)
            alertDialog.setCancelable(true)
            alertDialog.setCanceledOnTouchOutside(true)

        }
        catch (e:Exception)
        {
            e.printStackTrace()
        }

    }


    fun Show()
    {
        if(!alertDialog.isShowing)
        {
            val picker = view.findViewById<DatePicker>(R.id.datePicker)
            view.findViewById<TextView>(R.id.tvDate).text=  Convert.IntDateToStringDate(picker.year,picker.month+1, picker.dayOfMonth,"dd MMM, yyyy")
            alertDialog.show()

            alertDialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.window.setLayout((DeviceInfo.GetScreenSize(context).x * .94).toInt(), alertDialog.window.attributes.height)
        }
    }
    fun SetButtonText(buttonText:String): DatePickerDialog
    {
        view.findViewById<Button>(R.id.btnSubmit).text=buttonText
        return this
    }
    fun BackgroundDim(enable:Boolean): DatePickerDialog
    {
        var dim= if(enable) .4f else .07f
        alertDialog.window.setDimAmount(dim)
        return this
    }

    fun Close() {
        if (alertDialog.isShowing)
            alertDialog.dismiss()
    }

    interface ClickListener
    {
        fun onDateSelected(stringDate: String)
    }
}