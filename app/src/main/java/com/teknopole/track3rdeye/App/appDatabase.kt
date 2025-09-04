package com.teknopole.track3rdeye.App

import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.Database
import com.teknopole.track3rdeye.DAOs.TrackingConfigDao
import com.teknopole.track3rdeye.DAOs.TrackingDao
import com.teknopole.track3rdeye.ObjectModels.Tracking
import com.teknopole.track3rdeye.ObjectModels.TrackingDay
import com.teknopole.track3rdeye.ObjectModels.TrackingRestriction


@Database(entities = [Tracking::class,TrackingDay::class,TrackingRestriction::class], version = 1,exportSchema = false)
abstract class appDatabase : RoomDatabase() {
    companion object {
        private var INSTANCE: appDatabase?=null

        fun Instance(): appDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(app.appContext, appDatabase::class.java, "thirdeye_db")
                        // allow queries on the main thread.
                        // Don't do this on a real app! See PersistenceBasicSample for an example.
                        .allowMainThreadQueries()
                        .build()
            }
            return INSTANCE!!
        }
    }

    abstract fun trackingDao(): TrackingDao
    abstract fun trackingConfigDao(): TrackingConfigDao
}