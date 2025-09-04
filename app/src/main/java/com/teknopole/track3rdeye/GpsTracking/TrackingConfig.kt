package com.teknopole.track3rdeye.GpsTracking

import android.text.format.DateUtils
import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.App.appDatabase
import com.teknopole.track3rdeye.ObjectModels.TrackingDay
import com.teknopole.track3rdeye.ObjectModels.TrackingRestriction
import com.teknopole.track3rdeye.Utils.Convert
import java.text.SimpleDateFormat
import java.util.*

class TrackingConfig {
  companion object {
      fun GetTrackingStartTime():Calendar {
          val startTime = app.GetUserSession().trackingStartTime
          val startCalendar =Calendar.getInstance(Locale.getDefault())
          startCalendar.timeInMillis = System.currentTimeMillis()
          startCalendar.set(Calendar.HOUR_OF_DAY, startTime.substring(0,startTime.indexOfFirst { x-> x==':' }).toInt())
          startCalendar.set(Calendar.MINUTE,startTime.substring(startTime.indexOfFirst { x-> x==':' } +1,startTime.indexOfLast { x-> x==':' }).toInt())
          startCalendar.set(Calendar.SECOND, 1)

          return startCalendar
      }
      fun GetTrackingEndTime():Calendar {
          val endTime = app.GetUserSession().trackingEndTime
          val endCalendar = Calendar.getInstance(Locale.getDefault())
          endCalendar.timeInMillis = System.currentTimeMillis()
          endCalendar.set(Calendar.HOUR_OF_DAY, endTime.substring(0,endTime.indexOfFirst { x-> x==':' }).toInt())
          endCalendar.set(Calendar.MINUTE, endTime.substring(endTime.indexOfFirst { x-> x==':' } +1,endTime.indexOfLast { x-> x==':' }).toInt())
          endCalendar.set(Calendar.SECOND, 1)

          return endCalendar
      }

      // Tracking Days
      fun GetTrackingDays():List<TrackingDay>{
          return appDatabase.Instance().trackingConfigDao().GetTrackingDays()
      }
      fun UpdateTrackingDays(trackingDays:List<TrackingDay>) {
          try {
              appDatabase.Instance().trackingConfigDao().apply {
                  DeleteTrackingDays()
                  InsertTrackingDays(trackingDays)
              }
          }catch (e:Exception)
          {
              e.printStackTrace()
          }
      }
      fun ClearTrackingDays() {
          try {
              appDatabase.Instance().trackingConfigDao().apply {
                  DeleteTrackingDays()
              }
          }catch (e:Exception)
          {
              e.printStackTrace()
          }
      }


      // Tracking Restrictions
      fun GetTrackingRestrictions():List<TrackingRestriction>{
          return appDatabase.Instance().trackingConfigDao().GetTrackingRestrictions()
      }
      fun UpdateTrackingRestrictions(trackingRestrictions:List<TrackingRestriction>) {
          try {
              appDatabase.Instance().trackingConfigDao().apply {
                  DeleteTrackingRestrictions()
                  InsertTrackingRestrictions(trackingRestrictions)
              }
          }catch (e:Exception)
          {
              e.printStackTrace()
          }
      }
      fun ClearTrackingRestrictions() {
          try {
              appDatabase.Instance().trackingConfigDao().apply {
                  DeleteTrackingRestrictions()
              }
          }catch (e:Exception)
          {
              e.printStackTrace()
          }
      }



      // Validations
      fun IsNowWorkingTime():Boolean{
          val startTime = GetTrackingStartTime()
          startTime.set(Calendar.SECOND,0)

          val endTime = GetTrackingEndTime()
          endTime.set(Calendar.SECOND,0)

          val currentTime = Calendar.getInstance(Locale.getDefault())
          return (currentTime >= startTime && currentTime < endTime)
      }
      fun IsTodayWorkingDay(): Boolean {
          var foundDate = false
          val currentDay = Calendar.getInstance(Locale.getDefault()).get(Calendar.DAY_OF_WEEK) //sat=0

          TrackingConfig.GetTrackingDays().forEach {
              if (it.day.equals(Convert.ParseIntDayToStringDay(currentDay),true))
              {
                  foundDate = true
              }
          }
          return foundDate
      }
      fun IsTodayRestrictionDay(): Boolean {
          TrackingConfig.GetTrackingRestrictions().forEach {
              if (it.restrictionEndDate.isNullOrEmpty())
              {
                  if (IsDateToday(it.restrictionStartDate,"yyyy-MM-dd'T'hh:mm:ss"))  // check is today is restriction date or not?
                  {
                      return true
                  }
              }else
              {
                  // date range
                 if (IsTodayWithinDateRange(it.restrictionStartDate,it.restrictionEndDate!!,"yyyy-MM-dd'T'hh:mm:ss"))
                 {
                     return true
                 }
              }
          }
          return false
      }


      // date conversion
      fun IsDateToday(date:String,pattern: String):Boolean {
          try {
              val dateFormat = SimpleDateFormat(pattern,Locale.getDefault())
              val calender = Calendar.getInstance(Locale.getDefault())

              calender.time = dateFormat.parse(date)
//              calender.set(Calendar.HOUR_OF_DAY,0)
//              calender.set(Calendar.MINUTE,0)
////              calender.set(Calendar.SECOND,0)
////              calender.set(Calendar.MILLISECOND,0)
//
//
//              val todayCalender = Calendar.getInstance(Locale.getDefault())
////              todayCalender.set(Calendar.HOUR_OF_DAY,0)
////              todayCalender.set(Calendar.MINUTE,0)
////              todayCalender.set(Calendar.SECOND,0)
////              todayCalender.set(Calendar.MILLISECOND,0)
//
//              DateUtils.isToday(calender.timeInMillis)
//             val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
//              val b =sdf.format(calender.time) == sdf.format(Calendar.getInstance(Locale.getDefault()))

              return  DateUtils.isToday(calender.timeInMillis)
          } catch (e: Exception) {
              e.printStackTrace()
          }
          return false
      }
      fun IsTodayWithinDateRange(startDate: String,endDate:String,pattern:String): Boolean {

          try {
              val dateFormat = SimpleDateFormat(pattern,Locale.getDefault())

              // start calender
              val startCalender = Calendar.getInstance(Locale.getDefault())
              startCalender.time = dateFormat.parse(startDate)
              startCalender.set(Calendar.HOUR_OF_DAY,0)
              startCalender.set(Calendar.MINUTE,0)
              startCalender.set(Calendar.SECOND,0)


              // end calender
              val endCalender = Calendar.getInstance(Locale.getDefault())
              endCalender.time = dateFormat.parse(endDate)
              endCalender.set(Calendar.HOUR_OF_DAY,23)
              endCalender.set(Calendar.MINUTE,59)
              endCalender.set(Calendar.SECOND,59)


              // today calender
              val todayCalender = Calendar.getInstance(Locale.getDefault())
              return  todayCalender.after(startCalender) && todayCalender.before(endCalender)
          } catch (e: Exception) {
              e.printStackTrace()
          }
          return false
      }
  }
}