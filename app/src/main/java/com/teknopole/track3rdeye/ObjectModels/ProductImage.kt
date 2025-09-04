package com.teknopole.track3rdeye.ObjectModels

import com.google.gson.annotations.SerializedName

data class ProductImage(
        @SerializedName("ProductId") var productId: Int,
        @SerializedName("Image") var image: String,
        @SerializedName("ImageRealName") var imageRealName: String,
        @SerializedName("AddedOn") var addedOn: String
)