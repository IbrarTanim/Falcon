package com.teknopole.track3rdeye.MVP.DataModels

import com.teknopole.track3rdeye.ObjectModels.ProductObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

class ProductListFragmentDataModel() {


    //    ******************** URL & Method **********************
    interface UrlClient {
        // agent check access
        @GET("Api/Mobile/GetProductListByCompanyId/{companyId}")
        fun GetProductListByCompanyId(
                @Path("companyId") companyId: Int
        ): Call<List<ProductObject>>
    }
}