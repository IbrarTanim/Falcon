package com.teknopole.track3rdeye.MVP.DataModels

import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.MVP.Contracts.ForceUserInfoUpdateContract
import com.teknopole.track3rdeye.ObjectModels.LoginResponse
import com.teknopole.track3rdeye.Utils.APIClient.Error
import com.teknopole.track3rdeye.Utils.APIClient.RESTClient
import com.teknopole.track3rdeye.Utils.APIClient.ResponsePacket
import com.teknopole.track3rdeye.Utils.APIClient.ServerResponse
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Path

class ForceUserInfoUpdateDataModel(private val presenter: ForceUserInfoUpdateContract.Presenter) : ForceUserInfoUpdateContract.DataModel {

    override fun UpdateUsernamePasswordRequest(employeeId: Int, newUsername: String, newPassword: String) {
        RESTClient.GetClient(UrlClient::class.java).UpdateUsernamePassword(employeeId, newUsername, newPassword)
                .enqueue(object : ServerResponse<ResponsePacket<LoginResponse>>(app.appContext) {
                    override fun OnComplete(response: ResponsePacket<LoginResponse>) {
                        presenter.OnUpdateUsernamePasswordRequestSuccess(response)
                    }

                    override fun OnError(error: Error) {
                        presenter.OnUpdateUsernamePasswordRequestFailed(error)
                    }
                })
    }

    //    ******************** URL & Method **********************
    interface UrlClient {
        // agent check access
        @POST("Api/Mobile/ChangeUsernameAndPasswordAfterFirstLogin/{createdBy}/{username}/{password}")
        fun UpdateUsernamePassword(
                @Path("createdBy") employeeId: Int,
                @Path("username") username: String,
                @Path("password") password: String
        ): Call<ResponsePacket<LoginResponse>>
    }
}