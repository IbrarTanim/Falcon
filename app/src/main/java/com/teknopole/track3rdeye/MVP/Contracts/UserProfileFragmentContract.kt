package com.teknopole.track3rdeye.MVP.Contracts

import android.graphics.Bitmap
import com.teknopole.track3rdeye.ObjectModels.Employee
import com.teknopole.track3rdeye.Utils.APIClient.Error
import com.teknopole.track3rdeye.Utils.APIClient.ResponsePacket
import java.io.File

interface UserProfileFragmentContract {
    interface Presenter {
        fun GetUserDetailsOnViewStarted()
        fun LoadDefaultImage()


        fun OnUpdateButtonClicked(emp: Employee)
        fun OnUpdateEmployeeResuestSuccess(responsePacket: ResponsePacket<Employee>)
        fun OnUpdateEmployeeRequestFailed(error: Error)

        fun OnRequestUploadProfilePic(imageFile: Bitmap)
        fun OnUploadProfilePicSuccess(responsePacket: ResponsePacket<String>)
        fun OnUploadProfilePicRequestFailed(error: Error)


    }


    interface View {
        fun SetUserDetailsToView(emp: Employee)
        fun ShowWarningToast(msg: String)
        fun ShowLoadingToast()
        fun CompleteLoadingToast(isSuccess: Boolean)
        fun ShowErrorToast(message: String)
        fun OnUpdateEmployeeSuccess()
        fun ChangeUpdateButtonEnabled(enable: Boolean)
        fun LoadEmployeeProfilePic(imageUrl: String)

    }



    interface Model {
        fun RequestUpdateEmployeeProfile(emp: Employee)
        fun RequestUploadProfilePic(userId: Int, imageFile: File)
    }
}