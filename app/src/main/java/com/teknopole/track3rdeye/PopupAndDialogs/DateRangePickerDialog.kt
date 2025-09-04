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
class DateRangePickerDialog(private val context:Context,starDate:String,endDate:String, private val listener: DateSelectListener) {
    private lateinit var alertDialog: AlertDialog

    private lateinit var view: View
    private lateinit var datePicker: DatePicker
    private lateinit var tvStartDate: TextView
    private lateinit var btnApply: Button
    private lateinit var tvEndDate: TextView
    private lateinit var tvErrMsg: TextView
    private var isStartDate: Boolean=true

    private val datePattern: String="dd MMM, yy"


    private var SD=0
    private var SM=0
    private var SY=0

    private var ED=0
    private var EM=0
    private var EY=0


    init {
        try {


            val startCal = Convert.StringDateToCalender(starDate,datePattern)
            val endCal = Convert.StringDateToCalender(endDate,datePattern)

            SD= startCal.get(Calendar.DAY_OF_MONTH)
            SM= startCal.get(Calendar.MONTH)+1
            SY= startCal.get(Calendar.YEAR)

            ED=endCal.get(Calendar.DAY_OF_MONTH)
            EM=endCal.get(Calendar.MONTH)+1
            EY=endCal.get(Calendar.YEAR)



             alertDialog = AlertDialog.Builder(context).create()
             view = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.date_range_picker_dialog_layout, null)


            btnApply = view.findViewById(R.id.btnSubmit)
            tvStartDate= view.findViewById(R.id.tvStartDate)
            tvEndDate= view.findViewById(R.id.tvEndDate)
            datePicker = view.findViewById(R.id.datePicker)
            tvErrMsg = view.findViewById(R.id.tvErrMsg)



            // init date picker
            initDatePicker()
            tvStartDate.setOnClickListener {
                isStartDate=true
                initDatePicker()
                it.isEnabled=false
                tvEndDate.isEnabled=true
            }
            tvEndDate.setOnClickListener {
                isStartDate=false
                initDatePicker()
                it.isEnabled=false
                tvStartDate.isEnabled=true
            }


            btnApply.setOnClickListener {
                if (isDateValid())
                {
                    listener.onDateRangeSelected(tvStartDate.text.toString(),tvEndDate.text.toString())
                    alertDialog.dismiss()
                }
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



    fun Show() {
        if(!alertDialog.isShowing)
        {
            initDatePicker()
            EnableOrDisableButtonBasedOnDateValidation()

            tvStartDate.text=  Convert.IntDateToStringDate(SY,SM, SD,datePattern)
            tvEndDate.text=  Convert.IntDateToStringDate(EY,EM, ED,datePattern)



            alertDialog.show()
            alertDialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.window.setLayout((DeviceInfo.GetScreenSize(context).x * .94).toInt(), alertDialog.window.attributes.height)
        }
    }
    fun SetButtonText(buttonText:String): DateRangePickerDialog {
        view.findViewById<Button>(R.id.btnSubmit).text=buttonText
        return this
    }
    fun BackgroundDim(enable:Boolean): DateRangePickerDialog {
        var dim= if(enable) .4f else .07f
        alertDialog.window.setDimAmount(dim)
        return this
    }


    private fun initDatePicker() {
        if (isStartDate)
        {
            datePicker
                    .init(SY,SM-1,SD)
                    // on date change
                    { _, y, m, d->

                        SD=d
                        SM=m+1
                        SY=y

                        tvStartDate.text=  Convert.IntDateToStringDate(SY,SM, SD,datePattern)

                        EnableOrDisableButtonBasedOnDateValidation()
                    }
        }else
        {
            datePicker
                    .init(EY,EM-1,ED)
                    // on date change
                    { p, y, m, d->
                        ED=d
                        EM=m+1
                        EY=y

                        tvEndDate.text=  Convert.IntDateToStringDate(EY,EM, ED,datePattern)

                        EnableOrDisableButtonBasedOnDateValidation()
                    }
        }
    }
    private fun isDateValid():Boolean {
        val startCal = Calendar.getInstance()
        val endCal = Calendar.getInstance()

        startCal.set(SY,SM,SD)
        endCal.set(EY,EM,ED)
        endCal.set(Calendar.HOUR_OF_DAY,23)
        endCal.set(Calendar.MINUTE,59)
        endCal.set(Calendar.SECOND,59)

        return startCal.before(endCal)
    }
    private fun EnableOrDisableButtonBasedOnDateValidation() {
        if (isDateValid())
        {
            tvErrMsg.visibility=View.GONE
            btnApply.alpha= 1f
            btnApply.isEnabled=true
        }else
        {
            tvErrMsg.visibility=View.VISIBLE
            btnApply.alpha= 0.6f
            btnApply.isEnabled=false
        }
    }

    fun Close() {
        if (alertDialog.isShowing)
            alertDialog.dismiss()
    }

    interface DateSelectListener {
        fun onDateRangeSelected(starDateText: String, endDateText: String)
    }
}