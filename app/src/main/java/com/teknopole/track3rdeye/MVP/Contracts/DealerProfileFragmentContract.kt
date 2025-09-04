package com.teknopole.track3rdeye.MVP.Contracts

import android.graphics.Bitmap
import com.teknopole.track3rdeye.ObjectModels.ContactPerson
import com.teknopole.track3rdeye.ObjectModels.DealerObject
import com.teknopole.track3rdeye.Utils.APIClient.Error
import com.teknopole.track3rdeye.Utils.APIClient.ResponsePacket
import java.io.File

interface DealerProfileFragmentContract {
    interface Presenter {
        fun RequestToLoadDealersList(dealerId: Int)

        fun OnGetDealerProfileRequestSuccess(response: DealerObject)
        fun OnGetDealerProfileRequestFailed(error: Error)

        //edit views
        fun OnEditDealerRequest(dealer: DealerObject, update :Boolean)
        fun OnEditDealerResuestSuccess(responsePacket: ResponsePacket<DealerObject>, update :Boolean)
        fun OnEditDealerRequestFailed(error: Error, update :Boolean)

        fun OnRequestEditContactPerson(contactPerson: ContactPerson, update :Boolean)
        fun OnEditContactPersonResuestSuccess(responsePacket: ResponsePacket<ContactPerson>, update :Boolean)
        fun OnEditContactPersonRequestFailed(error: Error, update :Boolean)


        fun OnUploadContactPersonPicSuccess(responsePacket: ResponsePacket<String>)
        fun OnUploadContactPersonPicRequestFailed(error: Error)
        fun OnRequestUploadContactPersonPic(contactPerson: ContactPerson, imageFile: Bitmap)
    }

    interface View {
        fun SetDealerDetailsToView(dealer: DealerObject)

        fun ShowWarningToast(msg: String)
        fun ShowLoadingToast()
        fun CompleteLoadingToast(isSuccess: Boolean)
        fun ShowErrorToast(message: String)


        //edit views
//        fun OnEditDealerSuccess()
//        fun OnSaveDealerSuccess()
//        fun OnEditContactPersonSuccess()
//        fun OnSaveContactPersonSuccess()
//        fun OnEditDealerContactPersonPicSuccess()

        fun ChangeUpdateButtonEnabled(enable: Boolean)

        fun OnEditDealerSuccess()
        fun OnEditContactPersonSuccess()
    }

    interface Model {
        fun RequestToGetDealerDetails(dealerId: Int)

        //edit actions
        fun RequestUpdateDealer(dealer: DealerObject)
        fun RequestSaveDealer(dealer: DealerObject)

        fun RequestSaveDealerContactPerson(contactPerson: ContactPerson)
        fun RequestUpdateDealerContactPerson(contactPerson: ContactPerson)

        fun RequestUploadDealerContactPersonPic(contactPersonId: Int, imageFile: File)
    }
}