package com.teknopole.track3rdeye.MVP.DataModels

import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.MVP.Contracts.DealerListFragmentContract
import com.teknopole.track3rdeye.ObjectModels.DealerObject
import com.teknopole.track3rdeye.Utils.APIClient.Error
import com.teknopole.track3rdeye.Utils.APIClient.RESTClient
import com.teknopole.track3rdeye.Utils.APIClient.ServerResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

class DealerListFragmentDataModel(private val presenter: DealerListFragmentContract.Presenter) : DealerListFragmentContract.DataModel {
    override fun GetDealersList(companyId: Int, status: String, type: String) {
        RESTClient.GetClient(UrlClient::class.java).GetDealersListByCompanyId(companyId)
                .enqueue(object : ServerResponse<List<DealerObject>>(app.appContext) {

                    override fun OnComplete(response: List<DealerObject>) {
                        presenter.OnGetDealersListRequestSuccess(response)
                    }

                    override fun OnError(error: Error) {
                        presenter.OnGetDealersListRequestFailed(error)
                    }
                })
    }

    //    ******************** URL & Method **********************
    interface UrlClient {
        // agent check access
        @GET("Api/Mobile/GetDealerListByCompanyId/{companyId}")
        fun GetDealersListByCompanyId(
                @Path("companyId") companyId: Int
        ): Call<List<DealerObject>>
    }
}