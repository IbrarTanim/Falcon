package com.teknopole.track3rdeye.MVP.Contracts

import android.content.Intent
import android.graphics.drawable.Drawable
import com.teknopole.track3rdeye.ObjectModels.DealerObject
import com.teknopole.track3rdeye.ObjectModels.OrderObject
import com.teknopole.track3rdeye.ObjectModels.TaskObject

/**
 * Created by TeknoPole-03 on 5/14/2018.
 */
interface HomeActivityContract {
    interface View {
        fun ShowSplashScreenFragment()
        fun ShowDevicePermissionFragment()
        fun ShowSignInFragment()


        fun ShowHomeFragment()
        fun ExitApp()
        fun SetActivityStartedFromNotification(isTrue: Boolean)


        fun ShowPrivacyPolicyFragment(requiredAccept: Boolean)
        fun ShowForceUserInfoUpdateFragment()

        //home fragment actions
        fun ShowNotificationsFragment()

        fun ShowTaskFragment()
        fun ShowTaskDetailsFragment(taskId: Int)

        fun ShowOrderFragment()
        fun ShowOrderDetailsFragment(orderId: Int)
        fun ShowAddNewOrderFragment(orderId: Int)

        fun ShowDealerFragment()
        fun ShowAddNewDealerProfileFragment(dealerId: Int)
        fun ShowDealerProfileFragment(dealerId: Int)

        fun ShowProductDetailsFragment(productId: Int)

        fun ShowUserProfileFragment()
        //isLoggedOut
        fun ShowUpdateUserProfileFragment(drawable: Drawable)


        fun MarkNotificationAsSeen(notificationId: Int)
        fun GetUnseenNotificationCount(empId: Int)
    }

    interface Controller {
        fun OnViewCreated(intent: Intent, isNewIntent: Boolean)
        fun OnSplashScreenComplete()
        fun OnAllDevicePermissionsGranted()

        fun OnSignInSuccess()
        fun SignOut()

        fun OnPrivacyPolicyButtonClicked()
        fun OnPrivacyPolicyAccepted()
        fun OnTrackingPausedResumedSwitchToggled(isOn: Boolean)
        fun OnForceUsernamePasswordUpdateSuccess()

        fun OnShowNotificationsBellClicked()

        fun OnShowTaskButtonClicked()
        fun OnTaskItemSelectedShowTaskDetailsFragment(task: TaskObject)


        fun OnShowOrderButtonClicked()
        fun OnOrderItemSelectedShowOrderDetailsFragment(task: OrderObject)
        fun OnEditOrderFragment(orderId: Int)


        fun OnDealerItemSelectedShowDealerDetailsFragment(task: DealerObject)
        fun OnEditDealerFragment(dealerId: Int)

        fun OnShowProductDetailsFragment(task: Int)


        fun OnShowHistoryButtonClicked()
//        fun OnHistoryItemSelectedShowHistoryDetailsFragment(task: TaskObject)

        fun OnShowProfileButtonClicked()
        fun OnEditProfileMenuSelected(drawable: Drawable)


    }

}