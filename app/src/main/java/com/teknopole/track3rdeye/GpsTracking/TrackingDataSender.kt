package com.teknopole.track3rdeye.GpsTracking

import android.util.Log
import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.ObjectModels.Tracking
import com.teknopole.track3rdeye.Utils.APIClient.Error
import com.teknopole.track3rdeye.Utils.APIClient.RESTClient
import com.teknopole.track3rdeye.Utils.APIClient.ResponsePacket
import com.teknopole.track3rdeye.Utils.APIClient.ServerResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

class TrackingDataSender(private val listener: ActionListener) {
     fun RequestToPostTrackingData( trackings: List<Tracking>) {
        RESTClient.GetClient(UrlClient::class.java).PostTrackingData(trackings)
                .enqueue(object : ServerResponse<ResponsePacket<Tracking>>(app.appContext){
                    override fun OnComplete(response: ResponsePacket<Tracking>) {
                        listener.OnRequestToPostTrackingDataSuccess(response)
                    }
                    override fun OnError(error: Error) {
                       Log.d("Tracking send error",error.Message)
                    }
                })
    }


    //    ******************** URL & Method **********************
    interface UrlClient {
        // agent check access
        @POST("Api/Tracking/SaveTrackingData")
        fun PostTrackingData(@Body trackings: List<Tracking>): Call<ResponsePacket<Tracking>>
    }


    interface ActionListener{
        fun OnRequestToPostTrackingDataSuccess(response: ResponsePacket<Tracking>)
    }
}