package com.teknopole.track3rdeye.MVP.DataModels

import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.MVP.Contracts.PrivacyPolicyContract
import com.teknopole.track3rdeye.ObjectModels.LoginResponse
import com.teknopole.track3rdeye.Utils.APIClient.Error
import com.teknopole.track3rdeye.Utils.APIClient.RESTClient
import com.teknopole.track3rdeye.Utils.APIClient.ResponsePacket
import com.teknopole.track3rdeye.Utils.APIClient.ServerResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


class PrivacyPolicyDataModel(private val presenter: PrivacyPolicyContract.Presenter) : PrivacyPolicyContract.DataModel {
    override fun RequestDownloadPrivacyPolicyFile(fileName: String) {
        RESTClient.GetClient(UrlClient::class.java).DownloadPrivacyPolicy(fileName)
                .enqueue(object : ServerResponse<ResponseBody>(app.appContext) {
                    override fun OnComplete(response: ResponseBody) {
                        presenter.OnPrivacyPolicyDownloadSuccess(response)
                    }

                    override fun OnError(error: Error) {
                        presenter.OnPrivacyPolicyDownloadFailed(error)
                    }
                })
    }

    override fun RequestAcceptPrivacyPolicy(employeeId: Int) {
        RESTClient.GetClient(UrlClient::class.java).AcceptPrivacyPolicy(employeeId)
                .enqueue(object : ServerResponse<ResponsePacket<LoginResponse>>(app.appContext) {
                    override fun OnComplete(response: ResponsePacket<LoginResponse>) {
                        presenter.OnAcceptPrivacyPolicyRequestSuccess(response)
                    }

                    override fun OnError(error: Error) {
                        presenter.OnAcceptPrivacyPolicyRequestFailed(error)
                    }
                })
    }


    //    ******************** URL & Method **********************
    interface UrlClient {
        // accept privacy policy
        @POST("Api/Mobile/AcceptPrivacyPolicyByCP_Employee/{createdBy}")
        fun AcceptPrivacyPolicy(@Path("createdBy") employeeId: Int): Call<ResponsePacket<LoginResponse>>

        //  @GET("PrivacyPolicy/CP_PrivacyPolicy/{fileName}")
        @GET("Company_PrivacyPolicy/{fileName}")
        fun DownloadPrivacyPolicy(@Path("fileName") fileName: String): Call<ResponseBody>
    }
}