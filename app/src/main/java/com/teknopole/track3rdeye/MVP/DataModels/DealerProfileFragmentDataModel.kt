package com.teknopole.track3rdeye.MVP.DataModels

import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.MVP.Contracts.DealerProfileFragmentContract
import com.teknopole.track3rdeye.MVP.Presenters.DealerProfileFragmentPresenter
import com.teknopole.track3rdeye.ObjectModels.ContactPerson
import com.teknopole.track3rdeye.ObjectModels.DealerObject
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


class DealerProfileFragmentDataModel(private val presenter: DealerProfileFragmentPresenter) : DealerProfileFragmentContract.Model {
    override fun RequestSaveDealer(dealer: DealerObject) {
        RESTClient.GetClient(UrlClient::class.java).SaveDealer(dealer)
                .enqueue(object : ServerResponse<ResponsePacket<DealerObject>>(app.appContext) {
                    override fun OnComplete(responsePacket: ResponsePacket<DealerObject>) {
                        presenter.OnEditDealerResuestSuccess(responsePacket, false)
                    }

                    override fun OnError(error: Error) {
                        presenter.OnEditDealerRequestFailed(error, false)
                    }
                })
    }

    override fun RequestSaveDealerContactPerson(contactPerson: ContactPerson) {
        RESTClient.GetClient(UrlClient::class.java).SaveContactPerson(contactPerson)
                .enqueue(object : ServerResponse<ResponsePacket<ContactPerson>>(app.appContext) {
                    override fun OnComplete(responsePacket: ResponsePacket<ContactPerson>) {
                        presenter.OnEditContactPersonResuestSuccess(responsePacket, false)
                    }

                    override fun OnError(error: Error) {
                        presenter.OnEditContactPersonRequestFailed(error, false)
                    }
                })
    }

    override fun RequestUpdateDealerContactPerson(contactPerson: ContactPerson) {
        RESTClient.GetClient(UrlClient::class.java).UpdateContactPerson(contactPerson)
                .enqueue(object : ServerResponse<ResponsePacket<ContactPerson>>(app.appContext) {
                    override fun OnComplete(responsePacket: ResponsePacket<ContactPerson>) {
                        presenter.OnEditContactPersonResuestSuccess(responsePacket, true)
                    }

                    override fun OnError(error: Error) {
                        presenter.OnEditContactPersonRequestFailed(error, true)
                    }
                })
    }

    override fun RequestUploadDealerContactPersonPic(contactPersonId: Int, imageFile: File) {

        val reqFile = RequestBody.create(MediaType.parse("image/*"), imageFile)
        val multipartImageData = MultipartBody.Part.createFormData("upload", imageFile.name, reqFile)

        RESTClient.GetClient(UrlClient::class.java).UploadImage(contactPersonId, multipartImageData)
                .enqueue(object : ServerResponse<ResponsePacket<String>>(app.appContext) {
                    override fun OnComplete(responsePacket: ResponsePacket<String>) {
                        presenter.OnUploadContactPersonPicSuccess(responsePacket)
                    }

                    override fun OnError(error: Error) {
                        presenter.OnUploadContactPersonPicRequestFailed(error)
                    }
                })
    }

    override fun RequestUpdateDealer(dealer: DealerObject) {
        RESTClient.GetClient(UrlClient::class.java).UpdateDealer(dealer)
                .enqueue(object : ServerResponse<ResponsePacket<DealerObject>>(app.appContext) {
                    override fun OnComplete(responsePacket: ResponsePacket<DealerObject>) {
                        presenter.OnEditDealerResuestSuccess(responsePacket, true)
                    }

                    override fun OnError(error: Error) {
                        presenter.OnEditDealerRequestFailed(error, true)
                    }
                })
    }

    override fun RequestToGetDealerDetails(dealerId: Int) {
        RESTClient.GetClient(UrlClient::class.java).GetDealerDetails(dealerId)
                .enqueue(object : ServerResponse<DealerObject>(app.appContext) {
                    override fun OnComplete(response: DealerObject) {
                        presenter.OnGetDealerProfileRequestSuccess(response)
                    }

                    override fun OnError(error: Error) {
                        presenter.OnGetDealerProfileRequestFailed(error)
                    }
                })
    }


    //    ******************** URL & Method **********************
    interface UrlClient {
        @GET("Api/Mobile/GetDealerDetailsByDealerId/{dealerId}")
        fun GetDealerDetails(
                @Path("dealerId") dealerId: Int
        ): Call<DealerObject>

        // agent check access
        @POST("Api/Mobile/UpdateDealer")
        fun UpdateDealer(@Body dealer: DealerObject): Call<ResponsePacket<DealerObject>>

        // agent check access
        @POST("Api/Mobile/SaveDealer")
        fun SaveDealer(@Body dealer: DealerObject): Call<ResponsePacket<DealerObject>>

        // agent check access
        @POST("Api/Mobile/UpdateDealerContactPerson")
        fun UpdateContactPerson(@Body contactPerson: ContactPerson): Call<ResponsePacket<ContactPerson>>

        // agent check access
        @POST("Api/Mobile/SaveDealerContactPerson")
        fun SaveContactPerson(@Body contactPerson: ContactPerson): Call<ResponsePacket<ContactPerson>>

        @Multipart
        @POST("Api/Mobile/UploadDealerContactPersonPhoto/{contactPersonId}")
        fun UploadImage(@Path("contactPersonId") contactPersonId:Int, @Part image: MultipartBody.Part ): Call<ResponsePacket<String>>
    }
}