package com.teknopole.track3rdeye.Utils.APIClient

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RESTClient {
    companion object {
        private val retrofit:Retrofit
        const val baseUrl ="http://thirdeye.akhtar3rdeye.com/"

        init {
            val httpClient = OkHttpClient.Builder()
            httpClient.readTimeout(10, TimeUnit.SECONDS)
            httpClient.connectTimeout(5, TimeUnit.SECONDS)

//           httpClient.addInterceptor(
//                   {
//                       val original = it.request()
//
//                       // Request customization: add request headers
//                       val requestBuilder = original.newBuilder()
//                               .addHeader("Authorization", "auth-value") // <-- this is the important line
//                               .addHeader("Accept", "Application/JSON")
//                       val request = requestBuilder.build()
//                       it.proceed(request)
//                   })

            val client = httpClient.build()

            retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build()
        }

       fun <T> GetClient(apiClientInterface: Class<T>): T {
           return retrofit.create(apiClientInterface)
       }

        fun GetInstance() :Retrofit
        {
            return retrofit
        }

        fun GetEmployeeImageUrl(imageName: String):String
        {
           return baseUrl+"Company_Images/Employee_Images/" +imageName
        }

        fun GetDefaultEmployeeImageUrl(imageName: String):String
        {
            return baseUrl+"Company_Images/Default_Images/" +imageName
        }
        fun GetDefaultEmployeeImageUrl(isMale: Boolean):String
        {
            return if (isMale) {
                baseUrl+"Company_Images/Default_Images/male.png"
            }else {
                baseUrl+ "Company_Images/Default_Images/female.png"
            }
        }
        fun GetCompanyImageUrl(imageName: String):String
        {
            return baseUrl+"Company_Images/Profile_Images/" + imageName
        }

        fun GetTaskAttachmentUrl(fileName: String):String
        {
            return baseUrl+"Company_Task_Attachments/" + fileName
        }

        fun GetProductImageUrl(imageName: String):String
        {
            return baseUrl+"Company_Product_Images/" + imageName
        }

        fun GetContactPersonImageUrl(imageName: String):String
        {
            return baseUrl+"Company_Images/DealerContactPerson_Images/" + imageName
        }
    }
}