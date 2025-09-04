package com.teknopole.track3rdeye.ObjectModels

import com.google.gson.annotations.SerializedName

class LoginResponse : Employee() {
    @SerializedName("PPAccepted")
    var privacyPolicyAccepted: String = ""
    @SerializedName("TrackingDays")
    var trackingDays: List<TrackingDay> = arrayListOf()
    @SerializedName("TrackingRestriction")
    var trackingRestriction: List<TrackingRestriction> = arrayListOf()
}