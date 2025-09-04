package com.teknopole.track3rdeye.MVP.Presenters

import android.content.Context
import android.os.Handler
import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.MVP.Contracts.SignInFragmentContract
import com.teknopole.track3rdeye.MVP.DataModels.SignInFragmentDataModel
import com.teknopole.track3rdeye.ObjectModels.LoginResponse
import com.teknopole.track3rdeye.Utils.APIClient.Error
import com.teknopole.track3rdeye.Utils.APIClient.ResponsePacket
import com.teknopole.track3rdeye.Utils.DeviceInfo

/**
 * Created by MD. ABDUR ROUF on 5/17/2018.
 */
class SignInFragmentPresenter(private val context: Context, private val view: SignInFragmentContract.View) : SignInFragmentContract.Controller {
    private val dataModel = SignInFragmentDataModel(context, this)

    override fun OnSignInButtonClicked(username: String, password: String) {
        when {
            username.isEmpty() -> view.ShowToast("Ops! Invalid username")
            password.isEmpty() -> view.ShowToast("Ops! Invalid password")
            else -> {
                view.ChangeSignInButtonActive(false)
                view.StartLoadingTaost()
                dataModel.SignInRequest(username, password, DeviceInfo.GetDeviceIMEI(context))
            }
        }
    }


    //=============== Invoked by DataModel =====================
    override fun OnSignInRequestSuccess(responsePacket: ResponsePacket<LoginResponse>) {
        view.ChangeSignInButtonActive(true)
        when {
            responsePacket.IsReport == "NotOk" -> view.ShowErrorToast(responsePacket.Message)
            responsePacket.IsReport == "CredentialMatchesButNoPermissionByTeknopole" -> view.ShowWarningToast(responsePacket.Message)
            responsePacket.IsReport == "CredentialMatchesButNoPermissionByCompany" -> view.ShowWarningToast(responsePacket.Message)
            responsePacket.IsReport == "credentialMatchesWithPermission" -> {
                app.UpdateUserSessionAndCongig(responsePacket.Content)


                view.StopLoadingToast(true)
                view.OnSignInSuccess()
            }
        }
    }


    override fun OnSignInRequestFailed(error: Error) {
        // view.StopLoadingToast(false)
        Handler().postDelayed(
                {
                    view.ShowWarningToast(error.Message)
                    view.ChangeSignInButtonActive(true)
                }
                , 220)

    }
}