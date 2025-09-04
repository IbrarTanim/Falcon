package com.teknopole.track3rdeye.MVP.DataModels

import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.MVP.Contracts.TaskDetailsFragmentContract
import com.teknopole.track3rdeye.ObjectModels.TaskObject
import com.teknopole.track3rdeye.Utils.APIClient.Error
import com.teknopole.track3rdeye.Utils.APIClient.RESTClient
import com.teknopole.track3rdeye.Utils.APIClient.ResponsePacket
import com.teknopole.track3rdeye.Utils.APIClient.ServerResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

class TaskDetailsFragmentDataModel(private val presenter: TaskDetailsFragmentContract.Presenter) : TaskDetailsFragmentContract.DataModel {
    override fun RequestToGetTaskDetails(taskId: Int) {
        RESTClient.GetClient(UrlClient::class.java).GetTaskDetails(taskId)
                .enqueue(object : ServerResponse<TaskObject>(app.appContext) {
                    override fun OnComplete(response: TaskObject) {
                        presenter.OnGetTaskDetailsRequestSuccess(response)
                    }

                    override fun OnError(error: Error) {
                        presenter.OnGetTaskDetailsRequestFailed(error)
                    }
                })
    }

    override fun RequestToAcknoledgeTask(taskId: Int, userId: Int) {
        RESTClient.GetClient(UrlClient::class.java).AcknowledgeTask(taskId, userId)
                .enqueue(object : ServerResponse<ResponsePacket<String>>(app.appContext) {
                    override fun OnComplete(response: ResponsePacket<String>) {

                    }

                    override fun OnError(error: Error) {

                    }
                })
    }

    override fun StartTask(taskId: Int, userId: Int) {
        RESTClient.GetClient(UrlClient::class.java).StartTask(taskId, userId)
                .enqueue(object : ServerResponse<ResponsePacket<String>>(app.appContext) {
                    override fun OnComplete(response: ResponsePacket<String>) {
                        presenter.OnTaskStartOrCompleteRequestSuccess(response)
                    }

                    override fun OnError(error: Error) {
                        presenter.OnTaskStartOrCompleteRequestFailed(error)
                    }
                })
    }

    override fun CompleteTask(taskId: Int, userId: Int) {
        RESTClient.GetClient(UrlClient::class.java).CompleteTask(taskId, userId)
                .enqueue(object : ServerResponse<ResponsePacket<String>>(app.appContext) {
                    override fun OnComplete(response: ResponsePacket<String>) {
                        presenter.OnTaskStartOrCompleteRequestSuccess(response)
                    }

                    override fun OnError(error: Error) {
                        presenter.OnTaskStartOrCompleteRequestFailed(error)
                    }
                })
    }

    //    ******************** URL & Method **********************
    interface UrlClient {
        @GET("Api/Mobile/GetTaskDetails/{taskId}")
        fun GetTaskDetails(
                @Path("taskId") taskId: Int
        ): Call<TaskObject>

        @POST("Api/Mobile/AcknowledgeTask/{taskId}/{createdBy}")
        fun AcknowledgeTask(
                @Path("taskId") taskId: Int,
                @Path("createdBy") employeeId: Int
        ): Call<ResponsePacket<String>>

        @POST("Api/Mobile/StartTask/{taskId}/{createdBy}")
        fun StartTask(
                @Path("taskId") taskId: Int,
                @Path("createdBy") employeeId: Int
        ): Call<ResponsePacket<String>>

        @POST("Api/Mobile/CompleteTask/{taskId}/{createdBy}")
        fun CompleteTask(
                @Path("taskId") taskId: Int,
                @Path("createdBy") employeeId: Int
        ): Call<ResponsePacket<String>>
    }
}