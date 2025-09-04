package com.teknopole.track3rdeye.Utils.APIClient

import com.google.gson.annotations.SerializedName

data class Error(
        @SerializedName("IsReport") val Report: String = "",
        @SerializedName("Message") val Message: String = ""
)