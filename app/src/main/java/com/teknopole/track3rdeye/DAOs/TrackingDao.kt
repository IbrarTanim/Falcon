package com.teknopole.track3rdeye.DAOs

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.teknopole.track3rdeye.ObjectModels.Tracking

@Dao
interface TrackingDao {
    @Insert
    fun InsertTracking(tracking: Tracking)

    @Query("SELECT * FROM trackings")
    fun GetAllTrackings(): List<Tracking>

    @Query("DELETE FROM trackings")
    fun DeleteAllTrackings()

}