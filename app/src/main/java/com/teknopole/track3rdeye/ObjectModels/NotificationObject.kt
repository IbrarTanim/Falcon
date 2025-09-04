package com.teknopole.track3rdeye.ObjectModels

import com.google.gson.annotations.SerializedName

class NotificationObject {
    var Id: Int = 0
    @SerializedName("NotificationId")
    var notificationId: Int = 0
    @SerializedName("Title")
    var title: String = ""
    @SerializedName("Message")
    var message: String = ""
    @SerializedName("Description")
    var description: String = ""
    @SerializedName("NotificationType")
    var notificationType: String = ""
    @SerializedName("GeneratedOn")
    var generatedOn: String = ""
    @SerializedName("Status")
    var status: String = ""
}