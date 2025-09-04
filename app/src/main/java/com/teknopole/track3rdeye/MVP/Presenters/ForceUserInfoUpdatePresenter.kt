package com.teknopole.track3rdeye.MVP.Presenters

import android.content.Context
import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.MVP.Contracts.ForceUserInfoUpdateContract
import com.teknopole.track3rdeye.MVP.DataModels.ForceUserInfoUpdateDataModel
import com.teknopole.track3rdeye.MVP.Views.HomeActivity
import com.teknopole.track3rdeye.ObjectModels.LoginResponse
import com.teknopole.track3rdeye.Utils.APIClient.Error
import com.teknopole.track3rdeye.Utils.APIClient.ResponsePacket

class ForceUserInfoUpdatePresenter(private val context: Context, private val view: ForceUserInfoUpdateContract.View) : ForceUserInfoUpdateContract.Presenter {
    private val dataModel = ForceUserInfoUpdateDataModel(this)


    override fun OnFinishButtonClicked(newUsername: String, newPassword: String, confirmPassword: String) {
        when {
            newUsername.isEmpty() -> HomeActivity.ShowWarningToast("New username is required !")
            newUsername.length < 6 || newUsername.length >= 12 -> HomeActivity.ShowWarningToast("Username can be minimum 6 and maximum 11 characters !")
            newPassword.isEmpty() -> HomeActivity.ShowWarningToast("New password is required !")
            newPassword.length < 4 || newPassword.length > 15 -> HomeActivity.ShowWarningToast("Password can be minimum 4 and maximum 15 characters !")
            confirmPassword.isEmpty() -> HomeActivity.ShowWarningToast("Confirm password is required !")
            newPassword != confirmPassword -> HomeActivity.ShowWarningToast("Your new password and confirm password did not match !")

            else -> {
                // update username & password
                HomeActivity.StartLoading()
                dataModel.UpdateUsernamePasswordRequest(app.GetUserSession().id, newUsername, newPassword)
            }
        }
    }


    //============ Data model =============
    override fun OnUpdateUsernamePasswordRequestSuccess(response: ResponsePacket<LoginResponse>) {
        when {
            response.IsReport == "Ok" -> {
                HomeActivity.ShowSuccessToast(response.Message)
                view.OnUpdateRequestSuccess()
            }
            response.IsReport == "NotOk" -> HomeActivity.ShowErrorToast(response.Message)
            response.IsReport == "EmployeeDoesNotExists" -> HomeActivity.ShowErrorToast(response.Message)
            response.IsReport == "NullUsernameOrPassword" -> HomeActivity.ShowErrorToast(response.Message)
            response.IsReport == "UsernameExists" -> HomeActivity.ShowWarningToast(response.Message)
        }
    }

    override fun OnUpdateUsernamePasswordRequestFailed(error: Error) {
        HomeActivity.ShowErrorToast(error.Message)
    }
}