package com.teknopole.track3rdeye.Utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.location.Geocoder
import android.location.Location
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v4.media.MediaBrowserCompat
import android.util.Base64
import android.util.Log
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.teknopole.track3rdeye.App.app
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by Developer on 2/10/2018.
 */
class Convert {
    companion object {
        fun DpToPixel(dp:Int):Int {
            val d = app.appContext.resources.displayMetrics.density
            return (dp * d).toInt()
        }

        fun DateFromNumber(year:Int,month:Int,day:Int): Date {
            val format = SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
            try {
               return format.parse("$year-$month-$day")
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return Date()
        }

        fun IntDateToStringDate(year:Int,month:Int,day:Int,pattern:String): String {
            val dateFormat = SimpleDateFormat(pattern,Locale.getDefault())
            try {
               return dateFormat.format(DateFromNumber(year, month, day))

            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return ""
        }



        fun UnixToStringDate(unixTime: Long, pattern: String): String {
            try {
                val updatedate = Date(unixTime * 1000)
                val format = SimpleDateFormat(pattern)
                return format.format(updatedate)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return ""
        }


        fun StringDateToCalender(stringDate: String, pattern: String): Calendar {
            val calender = Calendar.getInstance()
            try {
                val format = SimpleDateFormat(pattern, Locale.getDefault())
                val date = format.parse(stringDate)



                calender.time = date
            }catch (e:Exception)
            {
            }
            return calender
        }

        fun StringDateFormat(inputDate: String, inputPattern: String,outputPattern:String): String {
            return try {
                var format = SimpleDateFormat(inputPattern, Locale.getDefault())

                val date = format.parse(inputDate)
                format = SimpleDateFormat(outputPattern, Locale.getDefault())
                format.format(date)
            }catch (e:Exception) {
                ""
            }
        }
        fun FormatDateTime(inputDate: String,outputPattern:String): String {
            return try {
                StringDateFormat(inputDate,"yyyy-MM-dd'T'HH:mm:ss",outputPattern).replace("AM","am").replace("PM","pm")
            }catch (e:Exception) {
                ""
            }
        }




        fun StringDateToUnix(stringDate: String, pattern: String,dayStart:Boolean=true): Long {
            try {
                val format = SimpleDateFormat(pattern, Locale.getDefault())
                val date = format.parse(stringDate)
                val calender =  Calendar.getInstance()


                calender.time = date

                if (dayStart)
                {
                    calender.set(Calendar.HOUR_OF_DAY,0)
                    calender.set(Calendar.MINUTE,0)
                    calender.set(Calendar.SECOND,0)
                }else
                {
                    calender.set(Calendar.HOUR_OF_DAY,23)
                    calender.set(Calendar.MINUTE,59)
                    calender.set(Calendar.SECOND,59)
                }

                return   (calender.timeInMillis / 1000)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return 0
        }

        fun ParseIntDayToStringDay(day:Int):String {
            return when(day)
            {
                7-> "Saturday"
                1-> "Sunday"
                2-> "Monday"
                3-> "Tuesday"
                4-> "Wednesday"
                5-> "Thursday"
                6-> "Friday"
                else ->""
            }
        }

        // get distance
        fun GetLocationDistance(oldLoc: Location, newLoc: Location): Float {
            return oldLoc.distanceTo(newLoc)
        }

        // get speed
        fun GetSpeedPerSecond(distanceInMeter: Float, timeInSecond: Long): Float {
            var value = 0f
            try {
                value= distanceInMeter / timeInSecond
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (value < 0 || distanceInMeter == 0f) {
                value = 0f
            }

            return value
        }

        fun GetDateDifference(date1: Date, date2: Date, timeUnit: TimeUnit): Long {
            try {
                val diffInMillies = date2.time - date1.time
                return TimeUnit.SECONDS.convert(diffInMillies, timeUnit)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return 0
        }


        // get geo code
        fun GetGeoCodeAddress(lat:Double,lng:Double): String {
            var address = "No address found."
            try {
                if (Geocoder.isPresent()) {
                    if (DeviceInfo.IsInternetConnected(app.appContext)) {
                        val addr = Geocoder(app.appContext, Locale.getDefault()).getFromLocation(lat, lng, 1)
                        if (addr.size > 0) {
                            address = addr[0].getAddressLine(0)
                        }
                    }
                }else
                {
                    address = "Device does not support textual location fetching."
                }

            } catch (e: Exception) {
            }
            return address
        }


        //=============== Image ===================
        fun BitmapToFile(mBitmap: Bitmap): File? {
            try {
                val options = BitmapFactory.Options()

                // Decode bitmap with inSampleSize set
                options.inJustDecodeBounds = false
                val newBitmap = resize(mBitmap,200,200) //Bitmap.createScaledBitmap(mBitmap, 100, 100, true)
                val file = File(app.appContext.filesDir, "Image_"
                        + Random().nextInt() + ".PNG")
                val out = app.appContext.openFileOutput(file.name,
                        Context.MODE_PRIVATE)
                newBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                out.flush()
                out.close()

                return file
            } catch (e: Exception) {
               e.printStackTrace()
            }

            return null
        }

        private fun resize(image: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
            var image = image
            if (maxHeight > 0 && maxWidth > 0) {
                val width = image.width
                val height = image.height
                val ratioBitmap = width.toFloat() / height.toFloat()
                val ratioMax = maxWidth.toFloat() / maxHeight.toFloat()

                var finalWidth = maxWidth
                var finalHeight = maxHeight
                if (ratioMax > ratioBitmap) {
                    finalWidth = (maxHeight.toFloat() * ratioBitmap).toInt()
                } else {
                    finalHeight = (maxWidth.toFloat() / ratioBitmap).toInt()
                }
                image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true)
                return image
            } else {
                return image
            }
        }

        fun vectorToBitmap(@DrawableRes id : Int): BitmapDescriptor {
            val vectorDrawable: Drawable = ResourcesCompat.getDrawable(app.appContext.resources, id, null)
                    ?: return BitmapDescriptorFactory.defaultMarker()
            val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth,
                    vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
           // DrawableCompat.setTint(vectorDrawable, color)
            vectorDrawable.draw(canvas)
            return BitmapDescriptorFactory.fromBitmap(bitmap)
        }

        fun BitmapToByteArray(bitmap: Bitmap): ByteArray {
            val stream = ByteArrayOutputStream()
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return stream.toByteArray()
        }

        fun BitmapToBase64String(bitmap: Bitmap):String {
            return try {
                val byteArray= BitmapToByteArray(bitmap)
                Base64.encodeToString(byteArray,Base64.DEFAULT)
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }
        fun Base64StringToBitmap (base64String: String):Bitmap {
            var bitmap = Bitmap.createBitmap(100,100,Bitmap.Config.ARGB_8888)
            try {
                val b = Base64.decode(base64String, Base64.DEFAULT)
                if (b.isNotEmpty()) {
                    bitmap = BitmapFactory.decodeByteArray(b, 0, b.size)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return bitmap
        }
    }
}