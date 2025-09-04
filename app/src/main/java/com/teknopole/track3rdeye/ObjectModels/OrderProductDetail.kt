package com.teknopole.track3rdeye.ObjectModels

import com.google.gson.annotations.SerializedName

data class OrderProductDetail(
        @SerializedName("ProductId") var productId: Int=0,
        @SerializedName("ProductName") var productName: String="",
        @SerializedName("ProductCode") var productCode: String="",
        @SerializedName("UnitPrice") var unitPrice: Int =0,
        @SerializedName("SalesPrice") var salesPrice: Int =0,
        @SerializedName("OrderQty") var orderQty: Double=0.0,
        @SerializedName("TotalAmount") var totalAmount: Double=0.0,
        @SerializedName("CurrentStockQty") var currentStockQty: Int =0
)

