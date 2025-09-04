package com.teknopole.track3rdeye.ObjectModels

import com.google.gson.annotations.SerializedName

data class OrderPostObject(
        @SerializedName("OrderObj") var order: OrderObject,
        @SerializedName("OrderDetailObjs") var productDetails: List<OrderProductDetail>
)

