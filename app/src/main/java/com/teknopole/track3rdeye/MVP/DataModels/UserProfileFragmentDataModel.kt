package com.teknopole.track3rdeye.MVP.DataModels

import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.MVP.Contracts.UserProfileFragmentContract
import com.teknopole.track3rdeye.MVP.Presenters.UserProfileFragmentPresenter
import com.teknopole.track3rdeye.ObjectModels.Employee
import com.teknopole.track3rdeye.Utils.APIClient.Error
import com.teknopole.track3rdeye.Utils.APIClient.RESTClient
import com.teknopole.track3rdeye.Utils.APIClient.ResponsePacket
import com.teknopole.track3rdeye.Utils.APIClient.ServerResponse
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.io.File


class UserProfileFragmentDataModel(private val presenter: UserProfileFragmentPresenter) : UserProfileFragmentContract.Model {
    override fun RequestUpdateEmployeeProfile(emp: Employee) {
        RESTClient.GetClient(UrlClient::class.java).UpdateEmployee(emp)
                .enqueue(object : ServerResponse<ResponsePacket<Employee>>(app.appContext) {
                    override fun OnComplete(responsePacket: ResponsePacket<Employee>) {
                        presenter.OnUpdateEmployeeResuestSuccess(responsePacket)
                    }

                    override fun OnError(error: Error) {
                        presenter.OnUpdateEmployeeRequestFailed(error)
                    }
                })
    }

    override fun RequestUploadProfilePic(userId: Int, imageFile: File) {

        val reqFile = RequestBody.create(MediaType.parse("image/*"), imageFile)
        val multipartImageData = MultipartBody.Part.createFormData("upload", imageFile.name, reqFile)

        RESTClient.GetClient(UrlClient::class.java).UploadImage(userId, multipartImageData)
                .enqueue(object : ServerResponse<ResponsePacket<String>>(app.appContext) {
                    override fun OnComplete(responsePacket: ResponsePacket<String>) {
                        presenter.OnUploadProfilePicSuccess(responsePacket)
                    }

                    override fun OnError(error: Error) {
                        presenter.OnUploadProfilePicRequestFailed(error)
                    }
                })
    }

    //    ******************** URL & Method **********************
    interface UrlClient {
        // agent check access
        @POST("Api/Mobile/UpdateCP_EmployeeProfile")
        fun UpdateEmployee(@Body emp: Employee): Call<ResponsePacket<Employee>>


        @Multipart
        @POST("Api/Mobile/UploadCP_EmployeePhoto/{createdBy}")
        fun UploadImage(@Path("createdBy") empId: Int, @Part image: MultipartBody.Part): Call<ResponsePacket<String>>
    }
}