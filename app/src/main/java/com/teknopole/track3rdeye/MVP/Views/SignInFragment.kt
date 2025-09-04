package com.teknopole.track3rdeye.MVP.Views


import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.teknopole.track3rdeye.MVP.Contracts.SignInFragmentContract
import com.teknopole.track3rdeye.MVP.Presenters.SignInFragmentPresenter
import com.teknopole.track3rdeye.PopupAndDialogs.ForgetPasswordDialog

import com.teknopole.track3rdeye.R
import kotlinx.android.synthetic.main.fragment_sign_in.*


class SignInFragment : Fragment(), SignInFragmentContract.View, TextWatcher {
    private lateinit var presenter: SignInFragmentPresenter
    private var listener: FragmentActionListener? = null
    private var forgetPasswordDialog: ForgetPasswordDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initMember()
        initEvent()
    }

    override fun onPause() {
        super.onPause()
        forgetPasswordDialog?.Close()
    }

    private fun initMember() {
        presenter = SignInFragmentPresenter(context!!, this)
    }

    private fun initEvent() {
        btnSignIn.setOnClickListener { presenter.OnSignInButtonClicked(etUsername.text.toString(), etEmail.text.toString()) }
        tvForgetPassword.setOnClickListener {
            forgetPasswordDialog = ForgetPasswordDialog(context!!)
            forgetPasswordDialog?.Show()
        }
        etUsername.addTextChangedListener(this)
        etEmail.addTextChangedListener(this)
    }


    // text change listener
    override fun afterTextChanged(s: Editable?) {
        ChangeSignInButtonActive(!etUsername.text.isNullOrEmpty() && !etEmail.text.isNullOrEmpty())
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }


    //=============== Invoked by presenter ===============
    override fun ShowToast(message: String) {
        HomeActivity.ShowInfoToast(message)
    }

    override fun ShowErrorToast(message: String) {
        HomeActivity.ShowErrorToast(message)
    }

    override fun ShowWarningToast(message: String) {
        HomeActivity.ShowWarningToast(message)
    }

    override fun StartLoadingTaost() {
        HomeActivity.StartLoading()
    }

    override fun StopLoadingToast(success: Boolean) {
        HomeActivity.CompleteLoading(success)
    }

    override fun ChangeSignInButtonActive(enable: Boolean) {
        btnSignIn.isEnabled = enable
    }

    override fun OnSignInSuccess() {
        listener?.OnSignInSuccess()
    }


    // ============ Event ============
    fun SetEventListener(listenerFragment: FragmentActionListener) {
        this.listener = listenerFragment
    }

    interface FragmentActionListener {
        fun OnSignInSuccess()
    }
}
