package com.teknopole.track3rdeye.MVP.Views


import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.romainpiel.shimmer.Shimmer

import com.teknopole.track3rdeye.R
import kotlinx.android.synthetic.main.fragment_splash_screen.*

class SplashScreenFragment : Fragment() {
    private var listener: ActionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        Handler().postDelayed({
            listener?.OnTimeEnd()
        }, 3000)

        Shimmer().start(textView)
    }

    fun SetEventListener(listener: ActionListener) {
        this.listener = listener
    }

    interface ActionListener {
        fun OnTimeEnd()
    }
}