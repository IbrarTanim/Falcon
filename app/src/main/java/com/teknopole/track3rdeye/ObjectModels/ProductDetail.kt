package com.teknopole.track3rdeye.ObjectModels

import com.google.gson.annotations.SerializedName

data class ProductDetail(
        @SerializedName("Id") var id: Int,
        @SerializedName("Model") var model: String,
        @SerializedName("Label") var label: String,
        @SerializedName("LabelType") var labelType: String,
        @SerializedName("DataType") var dataType: String,
        @SerializedName("LabelName") var labelName: String,
        @SerializedName("Min") var min: Int,
        @SerializedName("Max") var max: Int,
        @SerializedName("Required") var required: Boolean,
        @SerializedName("MinErrorMessage") var minErrorMessage: String,
        @SerializedName("MaxErrorMessage") var maxErrorMessage: String,
        @SerializedName("ReqErrorMessage") var reqErrorMessage: String
)

