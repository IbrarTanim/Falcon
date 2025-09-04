package com.teknopole.track3rdeye.ObjectModels

import com.google.gson.annotations.SerializedName

data class TaskDescription(
        @SerializedName("TaskId") var taskId: Int = 0,
        @SerializedName("Description") var description: String = "",
        @SerializedName("DesciptionAddedOn") var descriptionAddedOn: String = ""
)