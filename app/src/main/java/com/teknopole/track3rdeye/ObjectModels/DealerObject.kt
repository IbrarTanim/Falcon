package com.teknopole.track3rdeye.ObjectModels

import com.google.gson.annotations.SerializedName

data class DealerObject(
        @SerializedName("Id") var id: Int = 0,
        @SerializedName("CompanyId") var companyId: Int = 0,
        @SerializedName("DealerName") var dealerName: String = "",
        @SerializedName("DealerMobile") var dealerMobile: String = "",
        @SerializedName("DealerPhone") var dealerPhone: String = "",
        @SerializedName("DealerEmail") var dealerEmail: String = "",
        @SerializedName("DealerAddress") var dealerAddress: String = "",
        @SerializedName("CreatedOn") var createdOn: String = "",
        @SerializedName("UpdatedOn") var updatedOn: String = "",
        @SerializedName("ContactPersons") var contactPersons: List<ContactPerson> = arrayListOf(),
        @SerializedName("CreatedBy") var createdBy: Int = 0
) {
    override fun toString(): String { //this is important for fitlering in autocomplete textview
        return dealerName
    }
}

