package com.teknopole.track3rdeye.ObjectModels

import com.google.gson.annotations.SerializedName

data class OrderObject(
        @SerializedName("Id") var id: Int =0,
        @SerializedName("OrderCode") var orderCode: String ="",
        @SerializedName("CompanyId") var companyId: Int =0,

        @SerializedName("EmployeeId") var employeeId: Int =0,
        @SerializedName("GrandTotalAmount") var grandTotalAmount: Double = 0.0,
        @SerializedName("CreatedOn") var createdOn: String ="",
        @SerializedName("ModifiedOn") var modifiedOn: String ="",
        @SerializedName("OrderStatus") var orderStatus: String ="",

        @SerializedName("DealerId") var dealerId: Int =0,
        @SerializedName("DealerName") var dealerName: String ="",
        @SerializedName("DealerMobile") var dealerMobile: String ="",
        @SerializedName("DealerPhone") var dealerPhone: String ="",
        @SerializedName("DealerEmail") var dealerEmail: String ="",
        @SerializedName("DealerAddress") var dealerAddress: String ="",

        @SerializedName("EmployeeUsername") var employeeUsername: String ="",
        @SerializedName("EmployeeFirstName") var employeeFirstName: String ="",
        @SerializedName("EmployeeLastName") var employeeLastName: String ="",
        @SerializedName("EmployeeFullName") var employeeFullName: String ="",
        @SerializedName("EmployeeDesignation") var employeeDesignation: String ="",
        @SerializedName("EmployeePhoto") var employeePhoto: String ="",

        @SerializedName("ProductDetails") var productDetails: List<OrderProductDetail> = arrayListOf()
)

