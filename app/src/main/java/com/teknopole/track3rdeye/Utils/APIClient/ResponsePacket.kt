package com.teknopole.track3rdeye.Utils.APIClient

class ResponsePacket<out T>(val IsReport:String, val Message:String, val Content:T)