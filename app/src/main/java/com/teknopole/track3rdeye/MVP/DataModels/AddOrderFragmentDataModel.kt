package com.teknopole.track3rdeye.MVP.DataModels

import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.MVP.Contracts.AddOrderFragmentContract
import com.teknopole.track3rdeye.MVP.Presenters.AddOrderFragmentPresenter
import com.teknopole.track3rdeye.ObjectModels.*
import com.teknopole.track3rdeye.Utils.APIClient.Error
import com.teknopole.track3rdeye.Utils.APIClient.RESTClient
import com.teknopole.track3rdeye.Utils.APIClient.ResponsePacket
import com.teknopole.track3rdeye.Utils.APIClient.ServerResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


class AddOrderFragmentDataModel(private val presenter: AddOrderFragmentPresenter) : AddOrderFragmentContract.Model {
    override fun RequestToGetOrderDetails(orderId: Int) {
        RESTClient.GetClient(OrderDetailsFragmentDataModel.UrlClient::class.java).GetOrderDetails(orderId)
                .enqueue(object : ServerResponse<OrderObject>(app.appContext) {
                    override fun OnComplete(response: OrderObject) {
                        presenter.OnGetOrderDetailsRequestSuccess(response)
                    }

                    override fun OnError(error: Error) {
                        presenter.OnGetOrderDetailsRequestFailed(error)
                    }
                })
    }

    override fun RequestToGetDealerList(cpProfileId: Int) {
        RESTClient.GetClient(DealerListFragmentDataModel.UrlClient::class.java).GetDealersListByCompanyId(cpProfileId)
                .enqueue(object : ServerResponse<List<DealerObject>>(app.appContext) {

                    override fun OnComplete(response: List<DealerObject>) {
                        presenter.OnGetDealersListRequestSuccess(response)
                    }

                    override fun OnError(error: Error) {
                        presenter.OnGetDealersListRequestFailed(error)
                    }
                })
    }


    override fun RequestToGetProductList(cpProfileId: Int) {
        RESTClient.GetClient(ProductListFragmentDataModel.UrlClient::class.java).GetProductListByCompanyId(cpProfileId)
                .enqueue(object : ServerResponse<List<ProductObject>>(app.appContext) {

                    override fun OnComplete(response: List<ProductObject>) {
                        presenter.OnGetProductListRequestSuccess(response)
                    }

                    override fun OnError(error: Error) {
                        presenter.OnGetProductListRequestFailed(error)
                    }
                })
    }

    override fun RequestAddNewOrder(order: OrderPostObject) {
        RESTClient.GetClient(UrlClient::class.java).CreateOrder(order)
                .enqueue(object : ServerResponse<ResponsePacket<OrderPostObject>>(app.appContext) {
                    override fun OnComplete(response: ResponsePacket<OrderPostObject>) {
                        presenter.OnAddOrderSuccess()
                    }

                    override fun OnError(error: Error) {
                        presenter.OnAddOrderFailed(error)
                    }
                })
    }


    override fun RequestUpdateOrder(order: OrderPostObject) {
        RESTClient.GetClient(UrlClient::class.java).UpdateOrder(order)
                .enqueue(object : ServerResponse<ResponsePacket<OrderPostObject>>(app.appContext) {
                    override fun OnComplete(response: ResponsePacket<OrderPostObject>) {
                        presenter.OnUpdateOrderSuccess()
                    }

                    override fun OnError(error: Error) {
                        presenter.OnUpdateOrderFailed(error)
                    }
                })
    }

    //    ******************** URL & Method **********************
    interface UrlClient {
        @POST("Api/Mobile/CreateOrder")
        fun CreateOrder(@Body order: OrderPostObject): Call<ResponsePacket<OrderPostObject>>

        @POST("Api/Mobile/UpdateOrder")
        fun UpdateOrder(@Body order: OrderPostObject): Call<ResponsePacket<OrderPostObject>>
    }
}