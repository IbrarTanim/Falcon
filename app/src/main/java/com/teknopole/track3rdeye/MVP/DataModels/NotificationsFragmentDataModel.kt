package com.teknopole.track3rdeye.MVP.DataModels

import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.MVP.Contracts.NotificationsFragmentContract
import com.teknopole.track3rdeye.MVP.Presenters.NotificationsFragmentPresenter
import com.teknopole.track3rdeye.ObjectModels.NotificationObject
import com.teknopole.track3rdeye.Utils.APIClient.Error
import com.teknopole.track3rdeye.Utils.APIClient.RESTClient
import com.teknopole.track3rdeye.Utils.APIClient.ResponsePacket
import com.teknopole.track3rdeye.Utils.APIClient.ServerResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

class NotificationsFragmentDataModel(private val presenter: NotificationsFragmentPresenter? = null, private val listener: ApiResponseListener? = null) : NotificationsFragmentContract.DataModel {

    override fun GetAllNotificationByEmployeeId(empId: Int) {
        RESTClient.GetClient(UrlClient::class.java).GetAllNotificationListByEmployeeId(empId)
                .enqueue(object : ServerResponse<List<NotificationObject>>(app.appContext) {
                    override fun OnComplete(response: List<NotificationObject>) {
                        presenter?.OnGetAllNotificationByEmployeeIdRequestSuccess(response)
                    }

                    override fun OnError(error: Error) {
                        presenter?.OnGetAllNotificationByEmployeeIdRequestFailed(error)
                    }
                }
                )
    }

    override fun RequestMarkNotificationAsSeen(notificationId: Int, empId: Int) {
        RESTClient.GetClient(UrlClient::class.java).UpdateNotificationStatus(notificationId, empId)
                .enqueue(object : ServerResponse<ResponsePacket<Int>>(app.appContext) {
                    override fun OnComplete(response: ResponsePacket<Int>) {
                        try {
                            listener?.OnMarkNotificationAsSeenRequestSuccess(response)
                            presenter?.OnMarkNotificationAsSeenRequestSuccess(response)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    override fun OnError(error: Error) {
                        RequestMarkNotificationAsSeen(notificationId, empId)
                    }
                }
                )
    }

    fun GetCountOfUnseenNotificationsByEmployeeId(empId: Int) {
        RESTClient.GetClient(UrlClient::class.java).GetCountOfUnseenNotificationsByEmployeeId(empId)
                .enqueue(object : ServerResponse<Int>(app.appContext) {
                    override fun OnComplete(response: Int) {
                        try {
                            listener?.OnGetUnseenNotificationRequestSuccess(response)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    override fun OnError(error: Error) {
                    }
                }
                )
    }


    //    ******************** URL & Method **********************
    interface UrlClient {
        @GET("Api/Mobile/GetAllNotificationListByEmployeeId/{createdBy}")
        fun GetCountOfUnseenNotificationsByEmployeeId(
                @Path("createdBy") employeeId: Int
        ): Call<Int>

        @GET("Api/Mobile/GetAllNotificationListByEmployeeId/{createdBy}")
        fun GetAllNotificationListByEmployeeId(
                @Path("createdBy") employeeId: Int
        ): Call<List<NotificationObject>>

        @POST("Api/Mobile/UpdateNotificationStatus/{notificationId}/{createdBy}")
        fun UpdateNotificationStatus(
                @Path("notificationId") notificationId: Int,
                @Path("createdBy") employeeId: Int
        ): Call<ResponsePacket<Int>>
    }

    interface ApiResponseListener {
        fun OnMarkNotificationAsSeenRequestSuccess(response: ResponsePacket<Int>)
        fun OnGetUnseenNotificationRequestSuccess(unseenNotificationCount: Int)
    }
}