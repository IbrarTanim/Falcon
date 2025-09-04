package com.teknopole.track3rdeye.MVP.DataModels

import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.MVP.Contracts.OrderDetailsFragmentContract
import com.teknopole.track3rdeye.ObjectModels.OrderObject
import com.teknopole.track3rdeye.Utils.APIClient.Error
import com.teknopole.track3rdeye.Utils.APIClient.RESTClient
import com.teknopole.track3rdeye.Utils.APIClient.ResponsePacket
import com.teknopole.track3rdeye.Utils.APIClient.ServerResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

class OrderDetailsFragmentDataModel(private val presenter: OrderDetailsFragmentContract.Presenter) : OrderDetailsFragmentContract.DataModel {
    override fun RequestToGetOrderDetails(orderId: Int) {
        RESTClient.GetClient(UrlClient::class.java).GetOrderDetails(orderId)
                .enqueue(object : ServerResponse<OrderObject>(app.appContext) {
                    override fun OnComplete(response: OrderObject) {
                        presenter.OnGetOrderDetailsRequestSuccess(response)
                    }

                    override fun OnError(error: Error) {
                        presenter.OnGetOrderDetailsRequestFailed(error)
                    }
                })
    }



    //    ******************** URL & Method **********************
    interface UrlClient {
        @GET("Api/Mobile/GetOrderDetailsByOrderId/{orderId}")
        fun GetOrderDetails(
                @Path("orderId") orderId: Int
        ): Call<OrderObject>

    }
}