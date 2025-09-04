package com.teknopole.track3rdeye.ObjectModels

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import org.jetbrains.annotations.NotNull


@Entity(tableName = "trackings")
class Tracking {

    @PrimaryKey(autoGenerate = true)
    @NotNull
    var Id: Int = 0

    var EmployeeId: Int = 0
    var Lat: Double = 0.0
    var Lng: Double = 0.0
    var Ttimes: String = ""
    var Accuracy: Float = 0f
    var Distance: Float = 0f
    var Speed: Float = 0f

    var Modes: String = ""
    var Address: String = ""
    var IMEINumber: String = ""
    var CreatedAt: String = ""
    var GPSEnabled: String = ""
    var IsPaused: String = ""
    var IsLoggedIn: String = ""
    var InternetConnected: String = ""
    var BatteryPercentage: Int = 0

    override fun toString(): String {
        return "Emp:" + EmployeeId + " Lat:" + Lat + " Long:" + Lng + " gps:" + GPSEnabled + " loggedIn:" + IsLoggedIn + " wifi:" + InternetConnected + " Address:" + Address
    }
}