package com.teknopole.track3rdeye.MVP.Views


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.teknopole.track3rdeye.MVP.Contracts.ForceUserInfoUpdateContract
import com.teknopole.track3rdeye.MVP.Presenters.ForceUserInfoUpdatePresenter

import com.teknopole.track3rdeye.R
import kotlinx.android.synthetic.main.fragment_force_user_info_update.*

class ForceUserInfoUpdateFragment : Fragment(), ForceUserInfoUpdateContract.View {

    private lateinit var presenter: ForceUserInfoUpdatePresenter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_force_user_info_update, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter = ForceUserInfoUpdatePresenter(context!!, this)

        initEvent()
    }

    private fun initEvent() {
        btnFinish.setOnClickListener { presenter.OnFinishButtonClicked(etUsername.text.toString(), etNewPassword.text.toString(), etConfirmPassword.text.toString()) }
    }

    override fun OnUpdateRequestSuccess() {
        listener.OnUpdateUsernamePasswordSuccess()
    }


    // ============ Event ============

    private lateinit var listener: ActionListener
    fun SetEventListener(listenerFragment: ActionListener) {
        this.listener = listenerFragment
    }

    interface ActionListener {
        fun OnUpdateUsernamePasswordSuccess()
    }
}
