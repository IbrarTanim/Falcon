package com.teknopole.track3rdeye.MVP.Contracts

import com.teknopole.track3rdeye.ObjectModels.LoginResponse
import com.teknopole.track3rdeye.Utils.APIClient.Error
import com.teknopole.track3rdeye.Utils.APIClient.ResponsePacket

interface ForceUserInfoUpdateContract {
    interface View {
        fun OnUpdateRequestSuccess()

    }

    interface Presenter {
        fun OnFinishButtonClicked(newUsername: String, newPassword: String, confirmPassword: String)
        fun OnUpdateUsernamePasswordRequestSuccess(response: ResponsePacket<LoginResponse>)
        fun OnUpdateUsernamePasswordRequestFailed(error: Error)
    }

    interface DataModel {
        fun UpdateUsernamePasswordRequest(employeeId: Int, newUsername: String, newPassword: String)

    }
}