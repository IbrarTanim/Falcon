package com.teknopole.track3rdeye.ObjectModels

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.Nullable
import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.NotNull


@Entity(tableName = "trackingRestrictions")
class TrackingRestriction {
    @PrimaryKey(autoGenerate = true)
    @NotNull
    var Id: Int = 0


    @SerializedName("RestrictionStartDate")
    var restrictionStartDate: String = ""

    @Nullable
    @SerializedName("RestrictionEndDate")
    var restrictionEndDate: String? = ""

}