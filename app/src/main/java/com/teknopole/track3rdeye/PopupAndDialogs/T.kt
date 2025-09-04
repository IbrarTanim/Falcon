package com.teknopole.track3rdeye.PopupAndDialogs

import android.widget.Toast
import com.teknopole.track3rdeye.App.app

class T {
    companion object {
        fun show(msg:String)
        {
           Toast.makeText(app.appContext, msg,Toast.LENGTH_SHORT).show()
        }
    }
}