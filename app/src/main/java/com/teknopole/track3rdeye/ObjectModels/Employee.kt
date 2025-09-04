package com.teknopole.track3rdeye.ObjectModels

import com.google.gson.annotations.SerializedName

open class Employee {
    @SerializedName("Id")
    var id: Int = 0
    @SerializedName("Username")
    var username: String = ""
    @SerializedName("Password")
    var password: String = ""
    @SerializedName("FirstName")
    var firstName: String = ""
    @SerializedName("LastName")
    var lastName: String = ""
    @SerializedName("Designation")
    var designation: String = ""
    @SerializedName("Address")
    var address: String = ""
    @SerializedName("BirthDate")
    var birthDate: String = ""
    @SerializedName("Gender")
    var gender: String = ""
    @SerializedName("Mobile")
    var mobile: String = ""
    @SerializedName("Email")
    var email: String = ""
    @SerializedName("Photo")
    var photo: String = ""
    @SerializedName("IsActive")
    var isActive: Int = 0
    @SerializedName("PermissionStatus")
    var permissionStatus: String = ""
    @SerializedName("CompanyLogo")
    var companyLogo: String = ""
    @SerializedName("CompanyName")
    var companyName: String = ""
    @SerializedName("PrivacyPolicy")
    var privacyPolicy: String = ""
    @SerializedName("CP_ProfileId")
    var cpProfileId: Int = 0
    @SerializedName("TrackingInterval")
    var trackingInterval: Int = 30
    @SerializedName("TrackingEndTime")
    var trackingEndTime: String = "20:00:00"
    @SerializedName("TrackingStartTime")
    var trackingStartTime: String = "10:00:00"
    @SerializedName("CanPause")
    var canPause: String = "No"
    @SerializedName("TrackingHistoryEnabled")
    var trackingHistoryEnabled: String = "No"

}