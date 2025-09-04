package com.teknopole.track3rdeye.MVP.Presenters

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Handler
import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.Fcm.FcmMessagingService
import com.teknopole.track3rdeye.GpsTracking.GpsTrackingService
import com.teknopole.track3rdeye.MVP.Contracts.HomeActivityContract
import com.teknopole.track3rdeye.MVP.Views.HomeActivity
import com.teknopole.track3rdeye.ObjectModels.DealerObject
import com.teknopole.track3rdeye.ObjectModels.OrderObject
import com.teknopole.track3rdeye.ObjectModels.TaskObject
import com.teknopole.track3rdeye.Utils.DeviceInfo

/**
 * Created by Md. Abdur Rouf-03 on 5/14/2018.
 */
class HomeActivityPresenter(private val context: Context, private val view: HomeActivityContract.View) : HomeActivityContract.Controller {


    //================= Invoked by View ==================
    override fun OnViewCreated(intent: Intent, isNewIntent: Boolean) {
        try {// if activity from notification then clear that notification
            app.ClearNotification(intent.getIntExtra("notificationId", -1))
            view.MarkNotificationAsSeen(intent.getIntExtra("notificationMessageId", -1))
            view.GetUnseenNotificationCount(app.GetUserSession().id)

            val forceAction = if (intent.hasExtra("ClickAction")) intent.getStringExtra("ClickAction") else ""

            when {
                (forceAction == "" || (forceAction == "Home" && !isNewIntent)) -> view.ShowSplashScreenFragment()
                !DeviceInfo.IsAllPermissionsAreGranted(app.appContext) -> view.ShowDevicePermissionFragment()
                !app.isLoggedIn -> view.ShowSignInFragment()
                else -> {
                    // should take to home on back press
                    view.SetActivityStartedFromNotification(!isNewIntent)

                    // Notification action found
                    when (forceAction) {
                        "PrivacyPolicyChanged" -> view.ShowPrivacyPolicyFragment(true)

                        "TaskAssigned" -> {
                            try {
                                view.ShowTaskFragment()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        "TaskUpdated" -> {
                            try {
                                view.ShowTaskDetailsFragment(intent.getStringExtra("content").toInt())
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        "TaskDescriptionAdded" -> {
                            try {
                                view.ShowTaskDetailsFragment(intent.getStringExtra("content").toInt())
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        "AttachmentAdded" -> {
                            try {
                                view.ShowTaskDetailsFragment(intent.getStringExtra("content").toInt())
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        "AttachmentDeleted" -> {
                            try {
                                view.ShowTaskDetailsFragment(intent.getStringExtra("content").toInt())
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        "MemberAdded" -> {
                            try {
                                view.ShowTaskDetailsFragment(intent.getStringExtra("content").toInt())
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        "MemberRemoved" -> {
                            try {
                                view.ShowTaskDetailsFragment(intent.getStringExtra("content").toInt())
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    // Make decision and show fragment ------------------------- Entry point
    private fun DecideAndShowCorrectFragment() {
        when {
            app.GetUserSession().id <= 0 -> view.ShowSignInFragment()
            !app.IsPirvacyPolicyAccepted() -> view.ShowPrivacyPolicyFragment(true)
            app.GetUserSession().username.length >= 12 -> view.ShowForceUserInfoUpdateFragment()  // 7 will be change to 12
            else -> {
                view.ShowHomeFragment()

                // Start Gps Tracking - ***
                Handler().postDelayed({
                    GpsTrackingService.StartOrStopUserLocationUpdateBasedOnConfiguration(context)
                    GpsTrackingService.SetLocationServiceStartAndStopAlerm(context)
                }, 3000)


                // send fcm token to server
                FcmMessagingService.FcmUpdateRequest()
            }
        }
    }


    // splash screen & device permission page
    override fun OnSplashScreenComplete() {
        if (DeviceInfo.IsAllPermissionsAreGranted(context))
            DecideAndShowCorrectFragment()
        else
            view.ShowDevicePermissionFragment()
    }

    override fun OnAllDevicePermissionsGranted() {
        DecideAndShowCorrectFragment()
    }


    // home page
    override fun OnShowNotificationsBellClicked() {
        view.ShowNotificationsFragment()
    }

    override fun OnShowTaskButtonClicked() {
        view.ShowTaskFragment()
    }

    override fun OnShowProfileButtonClicked() {
        view.ShowUserProfileFragment()
    }

    override fun OnShowOrderButtonClicked() {
        view.ShowOrderFragment()
    }

    override fun OnShowHistoryButtonClicked() {
        view.ShowDealerFragment()
    }

    override fun OnEditOrderFragment(orderId: Int) {
        view.ShowAddNewOrderFragment(orderId)
    }

    override fun OnEditDealerFragment(dealerId: Int) {
        view.ShowAddNewDealerProfileFragment(dealerId)
    }

    //Bottom home drawer
    override fun OnTrackingPausedResumedSwitchToggled(isOn: Boolean) {
        app.SetIsTrackingPaused(isOn)
        if (isOn) {
            HomeActivity.ShowSuccessToast("GPS Tracking paused.")
        } else {
            HomeActivity.ShowSuccessToast("GPS Tracking started.")
        }
    }

    fun onSignOutRequest() {
        // Stop Gps Tracking ***
        GpsTrackingService.SignOutAndStopService(context)
    }

    override fun SignOut() {
        // Stop Gps Tracking ***
        GpsTrackingService.StopService(context)
        view.ShowSignInFragment()

        app.ClearAllNotifications()
        app.ClearUserSession()
        app.SetIsTrackingPaused(false)
    }

    override fun OnPrivacyPolicyButtonClicked() {
        view.ShowPrivacyPolicyFragment(false)
    }


    // Authentication
    override fun OnSignInSuccess() {
        DecideAndShowCorrectFragment()
    }


    // privacy policy page
    override fun OnPrivacyPolicyAccepted() {
        DecideAndShowCorrectFragment()
    }


    // force username password update page
    override fun OnForceUsernamePasswordUpdateSuccess() {
        // HomeActivity.ShowSuccessToast("Your credentials has been successfully updated. Please sign in with your new credentials.")
        SignOut()
    }

    //profile page
    override fun OnEditProfileMenuSelected(drawable: Drawable) {
        view.ShowUpdateUserProfileFragment(drawable)
    }

    // task page
    override fun OnTaskItemSelectedShowTaskDetailsFragment(task: TaskObject) {
        view.ShowTaskDetailsFragment(task.taskId)
    }

    // order page
    override fun OnOrderItemSelectedShowOrderDetailsFragment(order: OrderObject) {
        view.ShowOrderDetailsFragment(order.id)
    }

    override fun OnDealerItemSelectedShowDealerDetailsFragment(dealer: DealerObject) {
        view.ShowDealerProfileFragment(dealer.id)
    }

    override fun OnShowProductDetailsFragment(productId: Int) {
        view.ShowProductDetailsFragment(productId)
    }


}