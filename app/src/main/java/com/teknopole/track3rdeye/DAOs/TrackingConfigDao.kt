package com.teknopole.track3rdeye.DAOs

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.teknopole.track3rdeye.ObjectModels.TrackingDay
import com.teknopole.track3rdeye.ObjectModels.TrackingRestriction

@Dao
interface TrackingConfigDao {

    // Tracking days
    @Insert
    fun InsertTrackingDays(trackingDays: List<TrackingDay>)

    @Query("SELECT * FROM trackingDays")
    fun GetTrackingDays(): List<TrackingDay>

    @Query("DELETE FROM trackingDays")
    fun DeleteTrackingDays()



    // Tracking restriction
    @Insert
    fun InsertTrackingRestrictions(trackingRestrictions: List<TrackingRestriction>)

    @Query("SELECT * FROM trackingRestrictions")
    fun GetTrackingRestrictions(): List<TrackingRestriction>

    @Query("DELETE FROM trackingRestrictions")
    fun DeleteTrackingRestrictions()
}