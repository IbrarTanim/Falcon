package com.teknopole.track3rdeye.MVP.DataModels

import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.MVP.Contracts.ProductDetailsFragmentContract
import com.teknopole.track3rdeye.ObjectModels.ProductObject
import com.teknopole.track3rdeye.Utils.APIClient.Error
import com.teknopole.track3rdeye.Utils.APIClient.RESTClient
import com.teknopole.track3rdeye.Utils.APIClient.ResponsePacket
import com.teknopole.track3rdeye.Utils.APIClient.ServerResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

class ProductDetailsFragmentDataModel(private val presenter: ProductDetailsFragmentContract.Presenter) : ProductDetailsFragmentContract.DataModel {
    override fun RequestToGetProductDetails(productId: Int) {
        RESTClient.GetClient(UrlClient::class.java).GetProductDetails(productId)
                .enqueue(object : ServerResponse<ProductObject>(app.appContext) {
                    override fun OnComplete(response: ProductObject) {
                        presenter.OnGetProductDetailsRequestSuccess(response)
                    }

                    override fun OnError(error: Error) {
                        presenter.OnGetProductDetailsRequestFailed(error)
                    }
                })
    }

    //    ******************** URL & Method **********************
    interface UrlClient {
        @GET("Api/Mobile/GetProductDetailsByProductId/{productId}")
        fun GetProductDetails(
                @Path("productId") productId: Int
        ): Call<ProductObject>
    }
}