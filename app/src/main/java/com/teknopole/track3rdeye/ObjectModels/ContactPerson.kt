package com.teknopole.track3rdeye.ObjectModels

import com.google.gson.annotations.SerializedName

data class ContactPerson(
        @SerializedName("Id") var id: Int = 0,
        @SerializedName("DealerId") var dealerId: Int = 0,
        @SerializedName("CompanyId") var companyId: Int = 0,

        @SerializedName("ContactPersonName") var contactPersonName: String = "",
        @SerializedName("ContactPersonMobile") var contactPersonMobile: String = "",
        @SerializedName("ContactPersonPhone") var contactPersonPhone: String = "",
        @SerializedName("ContactPersonEmail") var contactPersonEmail: String = "",
        @SerializedName("ContactPersonDesignation") var contactPersonDesignation: String = "",
        @SerializedName("ContactPersonGender") var contactPersonGender: String = "",
        @SerializedName("ContactPersonPhoto") var contactPersonPhoto: String = "",
        @SerializedName("CreatedBy") var employeeId: Int = 0

)