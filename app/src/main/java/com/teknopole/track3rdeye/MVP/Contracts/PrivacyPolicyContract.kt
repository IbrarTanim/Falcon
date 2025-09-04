package com.teknopole.track3rdeye.MVP.Contracts

import com.teknopole.track3rdeye.ObjectModels.LoginResponse
import com.teknopole.track3rdeye.Utils.APIClient.Error
import com.teknopole.track3rdeye.Utils.APIClient.ResponsePacket
import okhttp3.ResponseBody

interface PrivacyPolicyContract {
    interface View {
        fun OnPrivacyPolicyAccepteSuccess()
        fun EnableAcceptButton()
        fun DisableAcceptButton()
        fun LoadPrivacyPolicy(bytes: ByteArray?)
        fun LoadPrivacyPolicy(url: String?)
        fun StopLoadingProgress()
        fun StartLoadingProgress()

    }

    interface Presenter {
        // from view
        fun OnPrivacyPolicyAcceptButtonClicked()


        // from data model
        fun OnAcceptPrivacyPolicyRequestSuccess(response: ResponsePacket<LoginResponse>)

        fun OnAcceptPrivacyPolicyRequestFailed(error: Error)
        fun LoadPrivacyPolicyFile()
        fun OnPrivacyPolicyDownloadSuccess(response: ResponseBody)
        fun OnPrivacyPolicyDownloadFailed(error: Error)
    }

    interface DataModel {
        fun RequestAcceptPrivacyPolicy(employeeId: Int)
        fun RequestDownloadPrivacyPolicyFile(fileName: String)

    }
}