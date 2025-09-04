package com.teknopole.track3rdeye.MVP.DataModels

import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.MVP.Contracts.TaskListFragmentContract
import com.teknopole.track3rdeye.ObjectModels.TaskObject
import com.teknopole.track3rdeye.Utils.APIClient.Error
import com.teknopole.track3rdeye.Utils.APIClient.RESTClient
import com.teknopole.track3rdeye.Utils.APIClient.ServerResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

class TaskListFragmentDataModel(private val presenter: TaskListFragmentContract.Presenter) : TaskListFragmentContract.DataModel {
    override fun GetTaskList(userId: Int, status: String, type: String) {
        RESTClient.GetClient(UrlClient::class.java).GetTaskListByEmployeeId(userId, status, type)
                .enqueue(object : ServerResponse<List<TaskObject>>(app.appContext) {
                    override fun OnComplete(response: List<TaskObject>) {
                        presenter.OnGetTaskListRequestSuccess(response)
                    }

                    override fun OnError(error: Error) {
                        presenter.OnGetTaskListRequestFailed(error)
                    }
                })
    }

    //    ******************** URL & Method **********************
    interface UrlClient {
        // agent check access
        @GET("Api/Mobile/GetTaskListByEmployeeIdAndStatusOrType/{createdBy}/{status}/{type}")
        fun GetTaskListByEmployeeId(
                @Path("createdBy") employeeId: Int,
                @Path("status") status: String,
                @Path("type") type: String
        ): Call<List<TaskObject>>
    }
}