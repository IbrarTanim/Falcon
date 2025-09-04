package com.teknopole.track3rdeye.Fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import android.widget.RemoteViews
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.GpsTracking.GpsTrackingService
import com.teknopole.track3rdeye.MVP.Views.HomeActivity
import com.teknopole.track3rdeye.ObjectModels.LoginResponse
import com.teknopole.track3rdeye.R
import com.teknopole.track3rdeye.Utils.APIClient.Error
import com.teknopole.track3rdeye.Utils.APIClient.RESTClient
import com.teknopole.track3rdeye.Utils.APIClient.ResponsePacket
import com.teknopole.track3rdeye.Utils.APIClient.ServerResponse
import com.teknopole.track3rdeye.Utils.LiveChange
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.*


class FcmMessagingService:FirebaseMessagingService() {
    companion object {
        // send fcm token to server
        fun FcmUpdateRequest() {
            Handler(Looper.getMainLooper()).post {
                try {
                    val jsonData = "{\"Id\":${app.GetUserSession().id},\"FCMToken\":\"${app.GetFcmToekn()}\"}"
                    val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonData)

                    RESTClient.GetClient(UrlClient::class.java).UpdateFrmToken(body)
                            .enqueue(object : ServerResponse<ResponsePacket<String>>(app.appContext){
                                override fun OnComplete(response: ResponsePacket<String>) {
                                    Log.e("Fcm Service",response.Message )
                                }
                                override fun OnError(error: Error) {
                                    Log.e("Fcm Service",error.Message )
                                }
                            })
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
    override fun onNewToken(fcmToken: String) {
        app.SetFcmToken(fcmToken)
        if (app.GetUserSession().id > 0)
        {
            FcmUpdateRequest()
        }
    }
    override fun onMessageReceived(message: RemoteMessage) {
          if (app.isLoggedIn)
          {
              TakeAction(message)
          }
    }
    private fun TakeAction(message: RemoteMessage) {
        message.data["CountOfUnseenNotification"]?.apply {
            if (this.toInt() >=0)
            {
                app.UpdateUnseenNotificationCount(this.toInt())
            }
        }
        when (message.data["ClickAction"]) {
            "None"-> ShowNotification(message.data,"None")
            "PrivacyPolicyChanged" -> PrivacyPolicyChanged(message)
            "TrackingSettingsChanged" -> TrackingSettingsChanged(message)
            "CompanyLogoUpdated"-> CompanyLogoUpdated(message)
            "EmployeeBlocked" -> EmployeeHasBeenBlocked(message)
            "TaskAssigned"-> NewTaskAssigned(message)
            "TaskUpdated"-> TaskUpdated(message)
            "TaskDescriptionAdded"-> TaskDescriptionAdded(message)
            "AttachmentAdded"-> TaskAttachmentAdded(message)
            "AttachmentDeleted"-> TaskAttachmentDeleted(message)
            "MemberAdded"-> TaskMemberAdded(message)
            "MemberRemoved"-> TaskMemberRemoved(message)
        }
    }





    private fun ShowNotification(data: MutableMap<String, String>, actionText: String="") {
        try {
            val notificationId = Random().nextInt(9999999)

            val notificationChannelId = notificationId.toString()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val nm = app.appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                nm.createNotificationChannel(NotificationChannel(notificationChannelId, "Fcm", NotificationManager.IMPORTANCE_HIGH))
            }

            val remoteView = RemoteViews(packageName,R.layout.notification_view_common)
            val remoteViewSmall = RemoteViews(packageName,R.layout.notification_view_common)

            remoteView.setTextViewText(R.id.tvTime,data["GeneratedOn"])
            remoteView.setTextViewText(R.id.tvTitle,data["Title"])
            remoteView.setTextViewText(R.id.tvMessage,data["Message"])

            remoteViewSmall.setTextViewText(R.id.tvTime,data["GeneratedOn"])
            remoteViewSmall.setTextViewText(R.id.tvTitle,data["Title"])
            remoteViewSmall.setTextViewText(R.id.tvMessage,data["Message"])





            val notification = NotificationCompat.Builder(app.appContext, notificationChannelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker(data["Title"])
                    .setContentTitle(data["Title"])
                    .setContentText(data["Message"])
                    .setCustomBigContentView(remoteView)
                    .setAutoCancel(true)
                    .setContent(remoteViewSmall)
                    .setLights(Color.RED, 3000, 3000)




            // notification click action
            if (actionText != "None" && actionText != "") {
                val activityIntent = Intent(applicationContext,HomeActivity::class.java).putExtra("ClickAction",actionText).putExtra("notificationId",notificationId)



                data["Content"]?.apply {
                    activityIntent .putExtra("content",this)
                }

                if (!data["NotificationId"].isNullOrEmpty())
                {
                    activityIntent.putExtra("notificationMessageId",  data["NotificationId"]?.toInt())
                }else
                {
                    activityIntent.putExtra("notificationMessageId", 0)
                }


                val pendingIntent = PendingIntent.getActivity(applicationContext, System.currentTimeMillis().toInt(), activityIntent,PendingIntent.FLAG_UPDATE_CURRENT)
                notification.setContentIntent(pendingIntent)
                remoteView.setOnClickPendingIntent(R.id.notificationView,pendingIntent)
                remoteViewSmall.setOnClickPendingIntent(R.id.notificationView,pendingIntent)
            }




            // sound
            if (data["Sound"] == "Y") {
                try {
                    notification.setSound(Uri.parse("android.resource://" + packageName + "/" + R.raw.tone_warning))

                    // val r = RingtoneManager.getRingtone(applicationContext, Uri.parse("android.resource://" + packageName + "/" + R.raw.tone_warning))
                    //                r.play()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            // vibration
            if (data["Vibration"] == "Y") {
                try {
                    //Vibration
                    notification.setVibrate(longArrayOf(10, 700, 100))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            NotificationManagerCompat.from(app.appContext).notify(notificationId, notification.build())
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }



    private fun CompanyLogoUpdated(message: RemoteMessage) {
        try {
            val conf = Gson().fromJson<LoginResponse>(message.data["Content"], LoginResponse::class.java)
            app.UpdateUserSessionAndCongig(conf)

            // live change
            LiveChange.HomeFragment.liveOnCompanyLogoUpdated()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun TrackingSettingsChanged(message: RemoteMessage) {
        try {
            ShowNotification(message.data,"Home")

            val conf = Gson().fromJson<LoginResponse>(message.data["Content"], LoginResponse::class.java)
            app.UpdateUserSessionAndCongig(conf)

            GpsTrackingService.RestertService(applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun PrivacyPolicyChanged(message: RemoteMessage) {
        try {
            ShowNotification(message.data, message.data["ClickAction"]!!)

            val conf = Gson().fromJson<LoginResponse>(message.data["Content"], LoginResponse::class.java)
            app.UpdateUserSessionAndCongig(conf)

            // live change
            LiveChange.HomeActivity.liveOnPrivacyPolicyChanged(message.data["Title"]!!, message.data["Message"]!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun EmployeeHasBeenBlocked(message: RemoteMessage) {
        try {
            ShowNotification(message.data,"Home")
            app.ClearUserSession()
            GpsTrackingService.StopService(app.appContext)
            LiveChange.HomeActivity.liveOnEmployeeHasBeenBlocked(message.data["Title"]!!, message.data["Message"]!!)
        }
        catch (e:Exception)
        {
            e.printStackTrace()
        }
    }


    private fun NewTaskAssigned(message: RemoteMessage) {
        try {
            ShowNotification(message.data, message.data["ClickAction"]!!)
            LiveChange.TaskListFragment.liveOnNewTaskAdded()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun TaskUpdated(message: RemoteMessage) {
        try {
            ShowNotification(message.data, message.data["ClickAction"]!!)
            LiveChange.TaskDetailsFragment.liveOnTaskUpdated(message.data["Content"]?.toInt()!!)
            LiveChange.TaskListFragment.liveOnTaskUpdated(message.data["Content"]?.toInt()!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun TaskDescriptionAdded(message: RemoteMessage) {
        try {
            ShowNotification(message.data, message.data["ClickAction"]!!)
            LiveChange.TaskDetailsFragment.liveOnTaskDescriptionAdded(message.data["Content"]?.toInt()!!)
            LiveChange.TaskListFragment.liveOnTaskDescriptionAdded(message.data["Content"]?.toInt()!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun TaskAttachmentAdded(message: RemoteMessage) {
        try {
            ShowNotification(message.data, message.data["ClickAction"]!!)
            LiveChange.TaskDetailsFragment.liveOnTaskAttachmentAdded(message.data["Content"]?.toInt()!!)
            LiveChange.TaskListFragment.liveOnTaskAttachmentAdded(message.data["Content"]?.toInt()!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun TaskAttachmentDeleted(message: RemoteMessage) {
        try {
            ShowNotification(message.data, message.data["ClickAction"]!!)
            LiveChange.TaskDetailsFragment.liveOnTaskAttachmentDeleted(message.data["Content"]?.toInt()!!)
            LiveChange.TaskListFragment.liveOnTaskAttachmentDeleted(message.data["Content"]?.toInt()!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun TaskMemberAdded(message: RemoteMessage) {
        try {
            ShowNotification(message.data, message.data["ClickAction"]!!)
            LiveChange.TaskDetailsFragment.liveOnTaskMemberAdded(message.data["Content"]?.toInt()!!)
            LiveChange.TaskListFragment.liveOnTaskMemberAdded(message.data["Content"]?.toInt()!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun TaskMemberRemoved(message: RemoteMessage) {
        try {
            ShowNotification(message.data, message.data["ClickAction"]!!)
            LiveChange.TaskDetailsFragment.liveOnTaskMemberRemoved(message.data["Content"]?.toInt()!!)
            LiveChange.TaskListFragment.liveOnTaskMemberRemoved(message.data["Content"]?.toInt()!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    //    ******************** URL & Method **********************
    interface UrlClient {
        @POST("Api/Mobile/UpdateFCMToken")
        fun UpdateFrmToken(@Body body:RequestBody): Call<ResponsePacket<String>>
    }
}