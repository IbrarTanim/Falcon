package com.teknopole.track3rdeye.ObjectModels

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull

@Entity(tableName = "trackingDays")
class TrackingDay {
    @PrimaryKey(autoGenerate = true)
    @NotNull
    var Id: Int = 0
    @SerializedName("Day")
    var day: String = ""
}