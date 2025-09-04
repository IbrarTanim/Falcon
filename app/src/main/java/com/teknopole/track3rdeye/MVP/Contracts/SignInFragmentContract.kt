package com.teknopole.track3rdeye.MVP.Contracts

import com.teknopole.track3rdeye.ObjectModels.LoginResponse
import com.teknopole.track3rdeye.Utils.APIClient.Error
import com.teknopole.track3rdeye.Utils.APIClient.ResponsePacket

/**
 * Created by MD. ABDUR ROUF on 5/17/2018.
 */
interface SignInFragmentContract {
    interface View {
        fun ShowToast(message: String)
        fun StartLoadingTaost()
        fun StopLoadingToast(success: Boolean)
        fun OnSignInSuccess()
        fun ShowErrorToast(message: String)
        fun ShowWarningToast(message: String)
        fun ChangeSignInButtonActive(enable: Boolean)

    }

    interface Controller {
        //        by View
        fun OnSignInButtonClicked(username: String, password: String)


        // by dataModel
        fun OnSignInRequestSuccess(responsePacket: ResponsePacket<LoginResponse>)

        fun OnSignInRequestFailed(error: Error)
    }

    interface Model {
        fun SignInRequest(username: String, password: String, deviceIMEI: String)
    }
}