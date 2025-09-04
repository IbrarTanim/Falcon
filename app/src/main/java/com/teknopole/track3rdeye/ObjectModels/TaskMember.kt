package com.teknopole.track3rdeye.ObjectModels

import com.google.gson.annotations.SerializedName

data class TaskMember(
        @SerializedName("EmployeeId") var employeeId: Int = 0,
        @SerializedName("GroupMemberFirstName") var groupMemberFirstName: String = "",
        @SerializedName("GroupMemberLastName") var groupMemberLastName: String = "",
        @SerializedName("GroupMemberDesignation") var groupMemberDesignation: String = "",
        @SerializedName("GroupMemberPhoto") var groupMemberPhoto: String = "",
        @SerializedName("GroupMemberGender") var groupMemberGender: String = "",
        @SerializedName("GroupMemberTaskAcknowledgement") var groupMemberTaskAcknowledgement: String = "",
        @SerializedName("GroupMemberTaskStatus") var groupMemberTaskStatus: String = "",
        @SerializedName("GroupMemberTaskSecondaryStatus") var groupMemberTaskSecondaryStatus: String = "",
        @SerializedName("GroupMemberStatus") var groupMemberStatus: Int = 0,
        @SerializedName("GroupMemberRemovalStatus") var groupMemberRemovalStatus: String = ""
)