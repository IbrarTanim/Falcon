package com.teknopole.track3rdeye.ObjectModels

import com.google.gson.annotations.SerializedName

data class TaskObject(
        @SerializedName("Id") var id: Int = 0,
        @SerializedName("TaskId") var taskId: Int = 0,
        @SerializedName("TaskTitle") var taskTitle: String = "",
        @SerializedName("TaskType") var taskType: String = "",
        @SerializedName("TaskStatus") var taskStatus: String = "",
        @SerializedName("TaskCode") var taskCode: String = "",
        @SerializedName("TaskLng") var taskLng: Double = 0.0,
        @SerializedName("TaskLat") var taskLat: Double = 0.0,
        @SerializedName("OverallTaskStatus") var overallTaskStatus: String = "",
        @SerializedName("OverallTaskAcknowledgement") var overallTaskAcknowledgement: String = "",
        @SerializedName("TaskCreatedOn") var taskCreatedOn: String = "",
        @SerializedName("TaskModifiedOn") var taskModifiedOn: String = "",
        @SerializedName("TaskStartTime") var taskStartTime: String = "",
        @SerializedName("TaskEndTime") var taskEndTime: String = "",
        @SerializedName("GroupName") var groupName: String = "",
        @SerializedName("NoOfGroupMembers") var noOfGroupMembers: Int = 0,
        @SerializedName("TaskDescriptions") var taskDescriptions: List<TaskDescription> = arrayListOf(),
        @SerializedName("TaskAttachments") var taskAttachments: List<TaskAttachment> = arrayListOf(),
        @SerializedName("TaskMembers") var taskMembers: List<TaskMember> = arrayListOf()
)


