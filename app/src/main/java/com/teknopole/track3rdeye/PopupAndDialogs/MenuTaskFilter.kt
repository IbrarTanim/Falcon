package com.teknopole.track3rdeye.PopupAndDialogs

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.MVP.Views.HomeActivity
import com.teknopole.track3rdeye.R
import com.teknopole.track3rdeye.Utils.DeviceInfo

/**
 * Created by MD. ABDUR ROUF on 5/24/2018.
 */
class MenuTaskFilter(private val context: Context,selected:String, private val listener: MenuSelectListener) {
    private val window: PopupWindow = PopupWindow(context)
    private val btnDateRangeSelector:Button
    private var radioButtonGroup: RadioGroup
    private var dataRandePickerDialog:DateRangePickerDialog?=null

    init {
        val v = LayoutInflater.from(context).inflate(R.layout.popup_menu_task_filter, null)
        window.contentView = v
        window.width =((DeviceInfo.displayWidth(app.appContext) * .85).toInt())
        window.height = RelativeLayout.LayoutParams.WRAP_CONTENT
        window.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT))
        window.isOutsideTouchable= true
        window.isFocusable=true


        btnDateRangeSelector = v.findViewById(R.id.btnDateRangeSelector)
        radioButtonGroup = v.findViewById(R.id.rdoGroup)


        btnDateRangeSelector.setOnClickListener {
            val dateRange = btnDateRangeSelector.text.toString()
            val idx = dateRange.indexOf("-",ignoreCase = true)
            val startDate = dateRange.substring(0,idx-1)
            val endDate = dateRange.substring(idx+2)

            dataRandePickerDialog = DateRangePickerDialog(context,startDate,endDate,object :DateRangePickerDialog.DateSelectListener{
            override fun onDateRangeSelected(starDateText: String, endDateText: String) {
                    btnDateRangeSelector.text = "$starDateText - $endDateText"
                }
            })
            dataRandePickerDialog?.BackgroundDim(false)
            dataRandePickerDialog?.Show()
        }


        // set selected
        when(selected)
        {
            "All" -> radioButtonGroup.check(R.id.rbAll)
            "Assigned" -> radioButtonGroup.check(R.id.rbAssigned)
            "In-Progress" -> radioButtonGroup.check(R.id.rbInProgress)
            "Complete" -> radioButtonGroup.check(R.id.rbComplete)
            "Group" -> radioButtonGroup.check(R.id.rbGroup)
            "Individual" -> radioButtonGroup.check(R.id.rdIndividual)
        }

        radioButtonGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId)
            {
                R.id.rbAll ->listener.FilterBy("All")
                R.id.rbAssigned ->listener.FilterBy("Assigned")
                R.id.rbInProgress ->listener.FilterBy("In-Progress")
                R.id.rbComplete ->listener.FilterBy("Complete")
                R.id.rbGroup ->listener.FilterBy("Group")
                R.id.rdIndividual ->listener.FilterBy("Individual")
            }
            Close()
        }


    }

     fun Close() {
        if (window.isShowing) {
            dataRandePickerDialog?.Close()
            window.dismiss()
        }
    }

    fun Show(view: View)
    {
        if (!window.isShowing)
            window.showAsDropDown(view,-(view.width-45),-20,Gravity.END)
    }

    interface MenuSelectListener
    {
        fun FilterBy(filterBy: String)
//        fun OnEditButtonClicked()
//        fun OnChangePasswordButtonClicked()
    }
}