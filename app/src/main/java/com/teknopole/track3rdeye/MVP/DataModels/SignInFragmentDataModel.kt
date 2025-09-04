package com.teknopole.track3rdeye.MVP.DataModels

import android.content.Context
import com.teknopole.track3rdeye.MVP.Contracts.SignInFragmentContract
import com.teknopole.track3rdeye.ObjectModels.LoginResponse
import com.teknopole.track3rdeye.Utils.APIClient.Error
import com.teknopole.track3rdeye.Utils.APIClient.RESTClient
import com.teknopole.track3rdeye.Utils.APIClient.ResponsePacket
import com.teknopole.track3rdeye.Utils.APIClient.ServerResponse
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by MD. ABDUR ROUF on 5/17/2018.
 */
class SignInFragmentDataModel(private val context: Context, private val controller: SignInFragmentContract.Controller) : SignInFragmentContract.Model {
    override fun SignInRequest(username: String, password: String, deviceIMEI: String) {
        RESTClient.GetClient(UrlClient::class.java).SignIn(username, password, deviceIMEI)
                .enqueue(object : ServerResponse<ResponsePacket<LoginResponse>>(context) {
                    override fun OnError(error: Error) {
                        controller.OnSignInRequestFailed(error)
                    }

                    override fun OnComplete(response: ResponsePacket<LoginResponse>) {
                        controller.OnSignInRequestSuccess(response)
                    }
                })
    }

    //    ******************** URL & Method **********************
    interface UrlClient {
        // agent check access
        @POST("Api/Mobile/LogIn/{username}/{password}/{imeiNumber}")
        fun SignIn(@Path("username") username: String,
                   @Path("password") password: String,
                   @Path("imeiNumber") imei: String
        ): Call<ResponsePacket<LoginResponse>>
    }
}