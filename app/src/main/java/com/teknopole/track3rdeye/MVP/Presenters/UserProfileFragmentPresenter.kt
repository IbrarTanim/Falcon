package com.teknopole.track3rdeye.MVP.Presenters

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.MVP.Contracts.UserProfileFragmentContract
import com.teknopole.track3rdeye.MVP.DataModels.UserProfileFragmentDataModel
import com.teknopole.track3rdeye.MVP.Views.HomeActivity
import com.teknopole.track3rdeye.ObjectModels.Employee
import com.teknopole.track3rdeye.Utils.APIClient.Error
import com.teknopole.track3rdeye.Utils.APIClient.RESTClient
import com.teknopole.track3rdeye.Utils.APIClient.ResponsePacket
import com.teknopole.track3rdeye.Utils.Convert
import com.teknopole.track3rdeye.Utils.Validator

class UserProfileFragmentPresenter(private val view: UserProfileFragmentContract.View) : UserProfileFragmentContract.Presenter {
    val dataModel: UserProfileFragmentContract.Model = UserProfileFragmentDataModel(this)
    override fun GetUserDetailsOnViewStarted() {
        val emp = app.GetUserSession()
        view.SetUserDetailsToView(emp)
        view.LoadEmployeeProfilePic(RESTClient.GetEmployeeImageUrl(app.GetUserSession().photo))
    }

    override fun OnUpdateButtonClicked(emp: Employee) {
        when {
            emp.firstName.isEmpty() -> view.ShowWarningToast("First name is required!")
            emp.lastName.isEmpty() -> view.ShowWarningToast("Last name is required!")
            emp.username.isEmpty() -> view.ShowWarningToast("Username is required!")
            emp.username.length < 6 || emp.username.length >= 12 -> HomeActivity.ShowWarningToast("Username can be minimum 6 and maximum 11 characters !")
            emp.email.isEmpty() -> view.ShowWarningToast("Email address is required!")
            emp.mobile.isEmpty() -> view.ShowWarningToast("Mobile number is required!")
            emp.birthDate.isEmpty() -> view.ShowWarningToast("Birth date is required!")
            emp.gender.isEmpty() -> view.ShowWarningToast("Gender is required!")
            emp.address.isEmpty() -> view.ShowWarningToast("Address is required!")
            !Validator.isEmailValid(emp.email) -> view.ShowWarningToast("Ops! Invalid email address")
            !Validator.isMobileNumberValid(emp.mobile) -> view.ShowWarningToast("Ops! Invalid mobile number")


            else -> {
                view.ChangeUpdateButtonEnabled(false)
                view.ShowLoadingToast()
                dataModel.RequestUpdateEmployeeProfile(emp)
            }
        }
    }

    override fun OnRequestUploadProfilePic(imageFile: Bitmap) {
        HomeActivity.StartLoading()
        Thread {

            val img = Convert.BitmapToFile(imageFile)
            if (img != null) {
                dataModel.RequestUploadProfilePic(app.GetUserSession().id, img)
            } else {
                Handler(Looper.getMainLooper()).post { HomeActivity.ShowInfoToast("Please select a valid image.") }
            }
        }.start()
    }

    override fun LoadDefaultImage() {
        if (app.GetUserSession().gender.equals("female", true)) {
            view.LoadEmployeeProfilePic(RESTClient.GetDefaultEmployeeImageUrl(false))
        } else {
            view.LoadEmployeeProfilePic(RESTClient.GetDefaultEmployeeImageUrl(true))
        }
    }

    override fun OnUpdateEmployeeResuestSuccess(responsePacket: ResponsePacket<Employee>) {
        when {
            responsePacket.IsReport == "Ok" -> {
                view.CompleteLoadingToast(true)
                app.SetUserSession(responsePacket.Content)
                view.OnUpdateEmployeeSuccess()
                return
            }
            responsePacket.IsReport == "NotOk" -> {
                view.ShowErrorToast(responsePacket.Message)
            }
            else -> view.ShowWarningToast(responsePacket.Message)
        }
        view.CompleteLoadingToast(false)
        view.ChangeUpdateButtonEnabled(true)
    }

    override fun OnUpdateEmployeeRequestFailed(error: Error) {
        view.ChangeUpdateButtonEnabled(true)
        view.CompleteLoadingToast(false)
        view.ShowErrorToast(error.Message)
    }

    override fun OnUploadProfilePicSuccess(responsePacket: ResponsePacket<String>) {
        when {
            responsePacket.IsReport == "Ok" -> {
                Handler(Looper.getMainLooper()).post {
                    try {
                        HomeActivity.ShowSuccessToast(responsePacket.Message)
                        app.UpdateEmployeeImage(responsePacket.Content)
                        view.LoadEmployeeProfilePic(RESTClient.GetEmployeeImageUrl(responsePacket.Content))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            else -> {
                HomeActivity.ShowErrorToast(responsePacket.Message)

            }
        }

    }

    override fun OnUploadProfilePicRequestFailed(error: Error) {
        Handler(Looper.getMainLooper()).post {
            HomeActivity.ShowErrorToast(error.Message)
        }
    }
}