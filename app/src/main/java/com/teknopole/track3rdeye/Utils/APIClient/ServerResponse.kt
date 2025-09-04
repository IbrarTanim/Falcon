package com.teknopole.track3rdeye.Utils.APIClient

import android.content.Context
import com.teknopole.track3rdeye.Utils.DeviceInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Created by Developer on 2/24/2018.
 */
abstract class ServerResponse<T>(private val context: Context) : Callback<T> {

    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response.isSuccessful && response.body() != null) {
            OnComplete(response.body()!!)
        }
        else
        {
            OnError(Error("ServerError",response.message()))
        }
//        else {
//            val converter =  RESTClient.Instance().responseBodyConverter<Error>(Error::class.java, arrayOfNulls<Annotation>(0))
//
//            var error: Error
//
//            try {
//                error = converter.convert(response.errorBody())
//
//
//                // if authentication failed then send broadcast
////                if (error.authenticationFailed)
////                {
////
////                    return
////                }
//
//
//                OnError(error)
//                return
//            } catch (e: Exception) {
//                e.printStackTrace()
//                // Unknow
//                error = Error("Unknown Error!")
//                OnError(error)
//            }
//
//        }
    }


    override fun onFailure(call: Call<T>, t: Throwable) {
        if (! DeviceInfo.IsInternetConnected(context))
            OnError(Error("ClientError","No Internet connection!"))
        else
           OnError(Error("ClientError","Ops! something error."))
    }


    abstract fun OnComplete(response: T)
    abstract fun OnError(error: Error)
}