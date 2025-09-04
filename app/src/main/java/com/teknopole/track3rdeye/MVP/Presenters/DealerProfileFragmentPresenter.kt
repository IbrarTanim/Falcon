package com.teknopole.track3rdeye.MVP.Presenters

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.MVP.Contracts.DealerProfileFragmentContract
import com.teknopole.track3rdeye.MVP.DataModels.DealerProfileFragmentDataModel
import com.teknopole.track3rdeye.MVP.Views.HomeActivity
import com.teknopole.track3rdeye.ObjectModels.ContactPerson
import com.teknopole.track3rdeye.ObjectModels.DealerObject
import com.teknopole.track3rdeye.Utils.APIClient.Error
import com.teknopole.track3rdeye.Utils.APIClient.RESTClient
import com.teknopole.track3rdeye.Utils.APIClient.ResponsePacket
import com.teknopole.track3rdeye.Utils.Convert
import com.teknopole.track3rdeye.Utils.Validator

class DealerProfileFragmentPresenter(private val view: DealerProfileFragmentContract.View) : DealerProfileFragmentContract.Presenter {

    val dataModel: DealerProfileFragmentContract.Model = DealerProfileFragmentDataModel(this)


    //============ Invoked by Data Model =============
    override fun OnGetDealerProfileRequestSuccess(dealer: DealerObject) {

        HomeActivity.CompleteLoading(true)
        view.SetDealerDetailsToView(dealer)
    }

    override fun OnGetDealerProfileRequestFailed(error: Error) {
        HomeActivity.ShowErrorToast(error.Message)
    }

    override fun RequestToLoadDealersList(dealerId: Int) {
        HomeActivity.StartLoading()
        Thread {
            dataModel.RequestToGetDealerDetails(dealerId)
        }.start()
    }

    override fun OnEditDealerRequest(dealer: DealerObject, update: Boolean) {
        when {
            dealer.dealerName.isEmpty() -> view.ShowWarningToast("Name is required!")
            dealer.dealerEmail.isEmpty() -> view.ShowWarningToast("Email address is required!")
            dealer.dealerMobile.isEmpty() -> view.ShowWarningToast("Mobile number is required!")
            dealer.dealerPhone.isEmpty() -> view.ShowWarningToast("Phone number is required!")
//            dealer.createdBy.isEmpty() -> view.ShowWarningToast("Birth date is required!")
//            dealer.companyId.isEmpty() -> view.ShowWarningToast("Gender is required!")
            dealer.dealerAddress.isEmpty() -> view.ShowWarningToast("Address is required!")
            !Validator.isEmailValid(dealer.dealerEmail) -> view.ShowWarningToast("Ops! Invalid email address")
            !Validator.isMobileNumberValid(dealer.dealerMobile) -> view.ShowWarningToast("Ops! Invalid mobile number")


            else -> {
                view.ChangeUpdateButtonEnabled(false)
                view.ShowLoadingToast()

                if (update) {
                    dataModel.RequestUpdateDealer(dealer)
                } else {
                    dataModel.RequestSaveDealer(dealer)
                }

            }
        }
    }

    override fun OnEditDealerResuestSuccess(responsePacket: ResponsePacket<DealerObject>, update: Boolean) {
        when {
            responsePacket.IsReport == "Ok" -> {
                view.CompleteLoadingToast(true)
                view.OnEditDealerSuccess()
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

    override fun OnEditDealerRequestFailed(error: Error, update: Boolean) {
        view.ChangeUpdateButtonEnabled(true)
        view.CompleteLoadingToast(false)
        view.ShowErrorToast(error.Message)
    }

    override fun OnRequestEditContactPerson(contactPerson: ContactPerson, update: Boolean) {
        when {
            contactPerson.contactPersonName.isEmpty() -> view.ShowWarningToast("First name is required!")
            contactPerson.contactPersonEmail.isEmpty() -> view.ShowWarningToast("Email address is required!")
//            contactPerson.contactPersonMobile.isEmpty() -> view.ShowWarningToast("Mobile number is required!")
            contactPerson.contactPersonPhone.isEmpty() -> view.ShowWarningToast("Mobile number is required!")
            contactPerson.contactPersonGender.isEmpty() -> view.ShowWarningToast("Gender is required!")
            contactPerson.contactPersonDesignation.isEmpty() -> view.ShowWarningToast("Designation is required!")
            !Validator.isEmailValid(contactPerson.contactPersonEmail) -> view.ShowWarningToast("Ops! Invalid email address")
            !Validator.isMobileNumberValid(contactPerson.contactPersonMobile) -> view.ShowWarningToast("Ops! Invalid mobile number")


            else -> {
                view.ChangeUpdateButtonEnabled(false)
                view.ShowLoadingToast()
                if (update) {
                    dataModel.RequestUpdateDealerContactPerson(contactPerson)
                } else {
                    dataModel.RequestSaveDealerContactPerson(contactPerson)
                }
            }
        }
    }

    override fun OnEditContactPersonResuestSuccess(responsePacket: ResponsePacket<ContactPerson>, update: Boolean) {
        when {
            responsePacket.IsReport == "Ok" -> {
                view.CompleteLoadingToast(true)

                view.OnEditContactPersonSuccess()
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

    override fun OnEditContactPersonRequestFailed(error: Error, update: Boolean) {
        view.ChangeUpdateButtonEnabled(true)
        view.CompleteLoadingToast(false)
        view.ShowErrorToast(error.Message)
    }

    override fun OnRequestUploadContactPersonPic(contactPerson: ContactPerson, imageFile: Bitmap) {
        HomeActivity.StartLoading()
        Thread {

            val img = Convert.BitmapToFile(imageFile)
            if (img != null) {
                dataModel.RequestUploadDealerContactPersonPic(contactPerson.id, img)
            } else {
                Handler(Looper.getMainLooper()).post { HomeActivity.ShowInfoToast("Please select a valid image.") }
            }
        }.start()
    }

    override fun OnUploadContactPersonPicSuccess(responsePacket: ResponsePacket<String>) {
        when {
            responsePacket.IsReport == "Ok" -> {
                Handler(Looper.getMainLooper()).post {
                    try {
                        HomeActivity.ShowSuccessToast(responsePacket.Message)
                        app.UpdateEmployeeImage(responsePacket.Content)
//                        view.LoadEmployeeProfilePic(RESTClient.GetEmployeeImageUrl(responsePacket.Content))
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

    override fun OnUploadContactPersonPicRequestFailed(error: Error) {
        Handler(Looper.getMainLooper()).post {
            HomeActivity.ShowErrorToast(error.Message)
        }
    }
}