package com.teknopole.track3rdeye.MVP.DataModels

import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.MVP.Contracts.OrderListFragmentContract
import com.teknopole.track3rdeye.ObjectModels.OrderObject
import com.teknopole.track3rdeye.Utils.APIClient.Error
import com.teknopole.track3rdeye.Utils.APIClient.RESTClient
import com.teknopole.track3rdeye.Utils.APIClient.ServerResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

class OrderListFragmentDataModel(private val presenter: OrderListFragmentContract.Presenter) : OrderListFragmentContract.DataModel {
    override fun GetOrderList(userId: Int, status: String, type: String) {
        RESTClient.GetClient(UrlClient::class.java).GetOrderListByEmployeeId(userId, status)
                .enqueue(object : ServerResponse<List<OrderObject>>(app.appContext) {
                    override fun OnComplete(response: List<OrderObject>) {
                        presenter.OnGetOrderListRequestSuccess(response)
                    }

                    override fun OnError(error: Error) {
                        presenter.OnGetOrderListRequestFailed(error)
                    }
                })
    }

    //    ******************** URL & Method **********************
    interface UrlClient {
        // agent check access
        @GET("Api/Mobile/GetOrderListByEmployeeIdAndStatus/{createdBy}/{status}")
        fun GetOrderListByEmployeeId(
                @Path("createdBy") employeeId: Int,
                @Path("status") status: String
        ): Call<List<OrderObject>>
    }
}