package com.teknopole.track3rdeye.MVP.Views

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.rouf69nb.library.ToastTime
import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.MVP.Contracts.HomeActivityContract
import com.teknopole.track3rdeye.MVP.DataModels.NotificationsFragmentDataModel
import com.teknopole.track3rdeye.MVP.Presenters.HomeActivityPresenter
import com.teknopole.track3rdeye.ObjectModels.DealerObject
import com.teknopole.track3rdeye.ObjectModels.OrderObject
import com.teknopole.track3rdeye.ObjectModels.TaskObject
import com.teknopole.track3rdeye.PopupAndDialogs.WarningDialog
import com.teknopole.track3rdeye.R
import com.teknopole.track3rdeye.Utils.APIClient.ResponsePacket
import com.teknopole.track3rdeye.Utils.DeviceInfo
import com.teknopole.track3rdeye.Utils.LiveChange
import kotlinx.android.synthetic.main.home_activity.*


class HomeActivity : AppCompatActivity(), HomeActivityContract.View, LiveChange.HomeActivity.LiveChangeListener {


    private lateinit var presenter: HomeActivityPresenter
    private var isActivityStartedFromNotification: Boolean = false
    private var warningDialog: WarningDialog? = null
    private var mp: MediaPlayer? = null


    companion object {
        var isRunning = false
        val touchPosition = Point(DeviceInfo.GetScreenSize(app.appContext).x / 2, DeviceInfo.GetScreenSize(app.appContext).y / 2)
        private var homeActivity: HomeActivity? = null
        var SuspressBackPress: Boolean = false

        fun ShowInfoToast(msg: String) {
            homeActivity?.showInfoToast(msg)
        }

        fun ShowSuccessToast(message: String) {
            homeActivity?.showSuccessToast(message)
        }

        fun ShowWarningToast(msg: String) {
            homeActivity?.showWarningToast(msg)
        }

        fun ShowErrorToast(msg: String) {
            homeActivity?.showErrorToast(msg)
        }

        fun StartLoading() {
            homeActivity?.showLoading()
        }

        fun CompleteLoading(success: Boolean) {
            homeActivity?.completeLoading(success)
        }

        fun ShowWarningDialog(title: String, msg: String) {
            homeActivity?.showWarningDialog(title, msg)
        }

        fun HideKeyboard(view: View) {
            homeActivity?.hideKeyboard(view)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeActivity = this
        setContentView(R.layout.home_activity)
        presenter = HomeActivityPresenter(this, this)
        presenter.OnViewCreated(intent, false)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        try {
            presenter.OnViewCreated(intent!!, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                touchPosition.x = ev.rawX.toInt()
                touchPosition.y = ev.rawY.toInt()
            }
        }

        return super.dispatchTouchEvent(ev)
    }

    override fun onPause() {
        super.onPause()
        warningDialog?.Close()
    }

    override fun onStart() {
        super.onStart()
        LiveChange.HomeActivity.registerLiveChangeListener(this, this)
    }

    override fun onStop() {
        super.onStop()
        LiveChange.HomeActivity.unregisterLiveChangeListener(this)
    }


    var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {

        if (HomeActivity.SuspressBackPress)
            return

        // handle if activity from notification then go to home fragment
        if (isActivityStartedFromNotification && supportFragmentManager.backStackEntryCount == 1) {
            try {
                supportFragmentManager.popBackStackImmediate()
                ShowHomeFragment()
                isActivityStartedFromNotification = false
                return
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        // if home page and back button clicked then show option to exit
        if (supportFragmentManager.backStackEntryCount <= 0) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                return
            }

            doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show()
            android.os.Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 1000)
        } else {
            super.onBackPressed()
        }
    }

    override fun ExitApp() {
        try {
            this.finish()
            System.exit(0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun showInfoToast(msg: String) {
        PlayToastSound(R.raw.tone_success)
        Handler().postDelayed({
            container.ShowToast(msg, ToastTime.Short, ContextCompat.getColor(this, R.color.colorInfo))
        }, 500)
    }

    private fun showSuccessToast(msg: String) {
        PlayToastSound(R.raw.tone_success)
        Handler().postDelayed({
            container.ShowToast(msg, ToastTime.Short, ContextCompat.getColor(this, R.color.colorSuccess))
        }, 500)
    }

    private fun showWarningToast(msg: String) {
        PlayToastSound(R.raw.tone_warning)
        Handler().postDelayed({
            container.ShowToast(msg, ToastTime.Short, ContextCompat.getColor(this, R.color.colorWarning))
        }, 500)
    }

    private fun showErrorToast(msg: String) {
        PlayToastSound(R.raw.tone_error)
        Handler().postDelayed({
            container.ShowToast(msg, ToastTime.Short, ContextCompat.getColor(this, R.color.colorError))
        }, 500)
    }

    private fun showLoading() {
        //container.StartLoading(ContextCompat.getColor(this,R.color.colorInfo))
        container.StartLoading(Color.parseColor("#F98E8C"))
    }

    private fun completeLoading(success: Boolean) {
        if (success) {
            PlayToastSound(R.raw.tone_success)
            Handler().postDelayed({
                val color = if (success) ContextCompat.getColor(this, R.color.colorSuccess) else ContextCompat.getColor(this, R.color.colorSuccess)
                container.CompleteLoading(success, color)
            }, 500)
        } else {
            PlayToastSound(R.raw.tone_error)
            Handler().postDelayed({
                val color = if (success) ContextCompat.getColor(this, R.color.colorSuccess) else ContextCompat.getColor(this, R.color.colorError)
                container.CompleteLoading(success, color)
            }, 500)
        }

    }

    private fun showWarningDialog(title: String, msg: String) {
        Handler(Looper.getMainLooper()).post {
            warningDialog = WarningDialog(this)
            warningDialog?.BackgroundDim(true)
            warningDialog?.SetMessageText(msg)
            warningDialog?.SetTitleText(title)
            warningDialog?.Show()
        }
    }

    // play toast sound
    private fun PlayToastSound(resId: Int) {
        Thread {
            try {
                mp = MediaPlayer.create(this, resId)
                mp?.setOnCompletionListener {
                    mp?.release()
                }
                mp?.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun hideKeyboard(view: View) {
        try {
            // Hide keyboard
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    //============== Invoked by presenter =============
    override fun SetActivityStartedFromNotification(isTrue: Boolean) {
        isActivityStartedFromNotification = isTrue
    }

    override fun MarkNotificationAsSeen(notificationId: Int) {
        if (notificationId > 0) {
            Thread {
                try {
                    NotificationsFragmentDataModel(listener = object : NotificationsFragmentDataModel.ApiResponseListener {
                        override fun OnMarkNotificationAsSeenRequestSuccess(response: ResponsePacket<Int>) {
                            Handler(Looper.getMainLooper()).post { app.UpdateUnseenNotificationCount(response.Content) }
                        }

                        override fun OnGetUnseenNotificationRequestSuccess(unseenNotificationCount: Int) {

                        }
                    })
                            .RequestMarkNotificationAsSeen(notificationId, app.GetUserSession().id)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }.start()
        }
    }

    override fun GetUnseenNotificationCount(empId: Int) {
        Thread {
            try {
                NotificationsFragmentDataModel(listener = object : NotificationsFragmentDataModel.ApiResponseListener {
                    override fun OnMarkNotificationAsSeenRequestSuccess(response: ResponsePacket<Int>) {

                    }

                    override fun OnGetUnseenNotificationRequestSuccess(unseenNotificationCount: Int) {
                        app.UpdateUnseenNotificationCount(unseenNotificationCount)
                    }
                }).GetCountOfUnseenNotificationsByEmployeeId(empId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    override fun ShowSplashScreenFragment() {
        val fragment = SplashScreenFragment()
        fragment.SetEventListener(object : SplashScreenFragment.ActionListener {
            override fun OnTimeEnd() {
                presenter.OnSplashScreenComplete()
            }
        })
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fade_in_animation, R.anim.fade_out_animation, R.anim.fade_in_animation, R.anim.fade_out_animation)
                .add(R.id.container, fragment, "SplashScreenFragment").commitAllowingStateLoss()
    }

    override fun ShowDevicePermissionFragment() {
        val fragment = DevicePermissionFragment()
        fragment.SetEventListener(object : DevicePermissionFragment.ActionListener {
            override fun OnAllPermissionsGranted() {
                presenter.OnAllDevicePermissionsGranted()
            }
        })
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fade_in_animation, R.anim.fade_out_animation, R.anim.fade_in_animation, R.anim.fade_out_animation)
                .replace(R.id.container, fragment, "DevicePermissionFragment").commitAllowingStateLoss()
    }

    override fun ShowSignInFragment() {
        val fragment = SignInFragment()
        fragment.SetEventListener(object : SignInFragment.FragmentActionListener {
            override fun OnSignInSuccess() {
                presenter.OnSignInSuccess()
            }
        })
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fade_in_animation, R.anim.fade_out_animation, R.anim.fade_in_animation, R.anim.fade_out_animation)
                .replace(R.id.container, fragment, "ShowSignInFragment").commitAllowingStateLoss()
    }


    override fun ShowPrivacyPolicyFragment(requiredAccept: Boolean) {
        val fragment = PrivacyPolicyFragment()
        fragment.SetEventListener(object : PrivacyPolicyFragment.ActionListener {
            override fun OnPrivacyPolicyAccepted() {
                presenter.OnPrivacyPolicyAccepted()
            }
        })

        val transition = supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fade_in_animation, R.anim.fade_out_animation, R.anim.fade_in_animation, R.anim.fade_out_animation)

        if (requiredAccept) {
            fragment.requiredAccept = true
            transition.replace(R.id.container, fragment, "PrivacyPolicyFragment").commitAllowingStateLoss()
        } else {
            fragment.requiredAccept = false
            transition.add(R.id.container, fragment, "PrivacyPolicyFragment")
                    .addToBackStack("PrivacyPolicyFragment")
                    .commitAllowingStateLoss()
        }
    }

    override fun ShowForceUserInfoUpdateFragment() {
        val fragment = ForceUserInfoUpdateFragment()
        fragment.SetEventListener(object : ForceUserInfoUpdateFragment.ActionListener {
            override fun OnUpdateUsernamePasswordSuccess() {
                presenter.OnForceUsernamePasswordUpdateSuccess()
            }
        })
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fade_in_animation, R.anim.fade_out_animation, R.anim.fade_in_animation, R.anim.fade_out_animation)
                .replace(R.id.container, fragment, "ForceUserInfoUpdateFragment").commitAllowingStateLoss()
    }

    override fun ShowHomeFragment() {
        val fragment = HomeFragment()
        fragment.SetEventListener(object : HomeFragment.HomeFragmentListener {


            override fun OnNotificationsBellClicked() {
                presenter.OnShowNotificationsBellClicked()
            }

            override fun OnSwitchTrackingToggled(isOn: Boolean) {
                presenter.OnTrackingPausedResumedSwitchToggled(isOn)
            }

            override fun OnDrawerPrivacyPolicyButtonClicked() {
                presenter.OnPrivacyPolicyButtonClicked()
            }

            override fun OnLogoutRequest() {
                presenter.onSignOutRequest()
            }

            override fun OnLoggedout() {
                presenter.SignOut()
            }

            override fun OnTaskButtonClicked() {
                presenter.OnShowTaskButtonClicked()
            }

            override fun OnHistoryButtonClicked() {
                presenter.OnShowHistoryButtonClicked()
            }

            override fun OnOrderButtonClicked() {
                presenter.OnShowOrderButtonClicked()
            }

            override fun OnProfileButtonClicked() {
                presenter.OnShowProfileButtonClicked()
            }

        })
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fade_in_animation, R.anim.fade_out_animation, R.anim.fade_in_animation, R.anim.fade_out_animation)
                .replace(R.id.container, fragment, "HomeFragment").commitAllowingStateLoss()
    }

    override fun ShowNotificationsFragment() {
        val fragment = NotificationsFragment()
        fragment.arguments = Bundle()

        fragment.SetEventListener(object : NotificationsFragment.ActionListener {

        })

        if (supportFragmentManager.findFragmentByTag("NotificationsFragment") == null || !supportFragmentManager.findFragmentByTag("NotificationsFragment").isVisible) {
            supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.fade_in_animation, R.anim.fade_out_animation, R.anim.fade_in_animation, R.anim.fade_out_animation)
                    .add(R.id.container, fragment, "NotificationsFragment")
                    .addToBackStack("NotificationsFragment")
                    .commitAllowingStateLoss()
        }
    }

    override fun ShowTaskFragment() {
        val fragment = TaskListFragment()
        fragment.arguments = Bundle()

        fragment.SetEventListener(object : TaskListFragment.ActionListener {
            override fun OnTaskItemSelectedShowTaskDetailsFragment(task: TaskObject) {
                presenter.OnTaskItemSelectedShowTaskDetailsFragment(task)
            }
        })

        if (supportFragmentManager.findFragmentByTag("TaskListFragment") == null || !supportFragmentManager.findFragmentByTag("TaskListFragment").isVisible || (supportFragmentManager.findFragmentByTag("TaskListFragment") as TaskListFragment).taskFilterBY != "Assigned") {
            supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.fade_in_animation, R.anim.fade_out_animation, R.anim.fade_in_animation, R.anim.fade_out_animation)
                    .add(R.id.container, fragment, "TaskListFragment")
                    .addToBackStack("TaskListFragment")
                    .commitAllowingStateLoss()
        }
    }

    override fun ShowTaskDetailsFragment(taskId: Int) {
        val fragment = TaskDetailsFragment()
        fragment.arguments = Bundle()
        fragment.arguments!!.putInt("taskId", taskId)

        fragment.SetEventListener(object : TaskDetailsFragment.ActionListener {
            override fun OnTaskStartOrCompleteRefreshTaskListFragment() {
                if (supportFragmentManager.findFragmentByTag("TaskListFragment") != null) {
                    (supportFragmentManager.findFragmentByTag("TaskListFragment") as TaskListFragment).RequestRefreshing()
                }
            }
        })

        val name = "TaskDetailsFragment"
        if (supportFragmentManager.findFragmentByTag(name) == null || !supportFragmentManager.findFragmentByTag(name).isVisible) {
            supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.fade_in_animation, R.anim.fade_out_animation, R.anim.fade_in_animation, R.anim.fade_out_animation)
                    .add(R.id.container, fragment, name)
                    .addToBackStack(name)
                    .commitAllowingStateLoss()
        } else {
            // If fragment already visible with a task data
            (supportFragmentManager.findFragmentByTag(name) as TaskDetailsFragment).apply {
                if (TaskId != taskId) {
                    //if showing another task data then update task details with selected task data
                    TaskId = taskId
                    (supportFragmentManager.findFragmentByTag(name) as TaskDetailsFragment).GetTaskDetails()
                }
            }
        }
    }

    override fun ShowDealerFragment() {
        val fragment = DealerListFragment()
        fragment.arguments = Bundle()

        fragment.SetEventListener(object : DealerListFragment.ActionListener {
            override fun OnDealerSelectedShowDealerProfileFragment(dealer: DealerObject) {
                presenter.OnDealerItemSelectedShowDealerDetailsFragment(dealer)
            }

            override fun OnFabClickedShowAddNewDealerFragment() {
                presenter.OnEditDealerFragment(0)
            }
        })

        val name = "DealerListFragment"
        if (supportFragmentManager.findFragmentByTag(name) == null || !supportFragmentManager.findFragmentByTag(name).isVisible || (supportFragmentManager.findFragmentByTag(name) as TaskListFragment).taskFilterBY != "Assigned") {
            supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.fade_in_animation, R.anim.fade_out_animation, R.anim.fade_in_animation, R.anim.fade_out_animation)
                    .add(R.id.container, fragment, name)
                    .addToBackStack(name)
                    .commitAllowingStateLoss()
        }
    }

    override fun ShowOrderFragment() {
        val fragment = OrderListFragment()
        fragment.arguments = Bundle()

        fragment.SetEventListener(object : OrderListFragment.ActionListener {
            override fun OnOrderItemSelectedShowOrderDetailsFragment(task: OrderObject) {
                presenter.OnOrderItemSelectedShowOrderDetailsFragment(task)
            }

            override fun OnFabClickedShowAddNewOrderFragment() {
                presenter.OnEditOrderFragment(0)
            }
        })

        val s = "OrderListFragment"
        if (supportFragmentManager.findFragmentByTag(s) == null || !supportFragmentManager.findFragmentByTag(s).isVisible || (supportFragmentManager.findFragmentByTag(s) as TaskListFragment).taskFilterBY != "Assigned") {
            supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.fade_in_animation, R.anim.fade_out_animation, R.anim.fade_in_animation, R.anim.fade_out_animation)
                    .add(R.id.container, fragment, s)
                    .addToBackStack(s)
                    .commitAllowingStateLoss()
        }
    }

    override fun ShowUserProfileFragment() {
        val fragment = UserProfileFragment()
        fragment.arguments = Bundle()

        fragment.SetEventListener(object : UserProfileFragment.ActionListener {
            override fun OnEditProfileMenuSelected(drawable: Drawable) {
                presenter.OnEditProfileMenuSelected(drawable)
            }
        })

        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fade_in_animation, R.anim.fade_out_animation, R.anim.fade_in_animation, R.anim.fade_out_animation)
                .add(R.id.container, fragment, "UserProfileFragment")
                .addToBackStack("UserProfileFragment").commitAllowingStateLoss()
    }


    override fun ShowUpdateUserProfileFragment(drawable: Drawable) {
        val fragment = UpdateUserProfileFragment()
        fragment.SetEventListener(object : UpdateUserProfileFragment.ActionListener {
            override fun OnUpdateEmployeeSuccess() {
                supportFragmentManager.popBackStackImmediate()
                supportFragmentManager.findFragmentByTag("UserProfileFragment").onStart()
            }
        })
        fragment.imageDrawable = drawable
        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fade_in_animation, R.anim.fade_out_animation, R.anim.fade_in_animation, R.anim.fade_out_animation)
                .add(R.id.container, fragment, "UpdateUserProfileFragment")
                .addToBackStack("UpdateUserProfileFragment").commitAllowingStateLoss()
    }

    override fun ShowDealerProfileFragment(dealerId: Int) {
        val fragment = DealerProfileFragment()
        fragment.arguments = Bundle()
        fragment.arguments!!.putInt("dealerId", dealerId)

        fragment.SetEventListener(object : DealerProfileFragment.ActionListener {
            override fun OnEditDealerSelected(dealerId: Int) {
                presenter.OnEditDealerFragment(dealerId)
            }

        })

        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fade_in_animation, R.anim.fade_out_animation, R.anim.fade_in_animation, R.anim.fade_out_animation)
                .add(R.id.container, fragment, "UserProfileFragment")
                .addToBackStack("UserProfileFragment").commitAllowingStateLoss()
    }

    override fun ShowAddNewDealerProfileFragment(dealerId: Int) {
        val fragment = AddDealerFragment()
        fragment.arguments = Bundle()
        fragment.arguments!!.putInt("dealerId", dealerId)

//        fragment.SetEventListener(object : AddDealerFragment.ActionListener{
//            override fun OnAddOrderSuccess() {
//                }
//        })

        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fade_in_animation, R.anim.fade_out_animation, R.anim.fade_in_animation, R.anim.fade_out_animation)
                .add(R.id.container, fragment, "UserProfileFragment")
                .addToBackStack("UserProfileFragment").commitAllowingStateLoss()
    }

    override fun ShowAddNewOrderFragment(orderId: Int) {
        val fragment = AddOrderFragment()
        fragment.arguments = Bundle()
        fragment.arguments!!.putInt("orderId", orderId)

        fragment.SetEventListener(object : AddOrderFragment.ActionListener {
            override fun OnProductInfoButtonClicked(product: Int) {
                presenter.OnShowProductDetailsFragment(product)

            }

            override fun OnDealerInfoButtonClicked(dealer: DealerObject) {
                presenter.OnDealerItemSelectedShowDealerDetailsFragment(dealer)
            }

            override fun OnAddOrderSuccess() {

            }
        })

        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fade_in_animation, R.anim.fade_out_animation, R.anim.fade_in_animation, R.anim.fade_out_animation)
                .add(R.id.container, fragment, "AddOrderFragment")
                .addToBackStack("AddOrderFragment").commitAllowingStateLoss()
    }


    override fun ShowOrderDetailsFragment(orderId: Int) {
        val fragment = OrderDetailsFragment()
        fragment.arguments = Bundle()
        fragment.arguments!!.putInt("orderId", orderId)

        fragment.SetEventListener(object : OrderDetailsFragment.ActionListener {
            override fun OnEditOrderSelected(orderId: Int) {
                presenter.OnEditOrderFragment(orderId)
            }

            override fun OnShowProductDetailsFragment() {

            }

            override fun OnShowDealerDetailsFragment() {

            }

            override fun OnSubmitOrder() {

            }
        })

        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fade_in_animation, R.anim.fade_out_animation, R.anim.fade_in_animation, R.anim.fade_out_animation)
                .add(R.id.container, fragment, "UserProfileFragment")
                .addToBackStack("UserProfileFragment").commitAllowingStateLoss()
    }


    override fun ShowProductDetailsFragment(productId: Int) {
        val fragment = ProductDetailsFragment()
        fragment.arguments = Bundle()
        fragment.arguments!!.putInt("productId", productId)

        fragment.SetEventListener(object : ProductDetailsFragment.ActionListener {

        })

        supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fade_in_animation, R.anim.fade_out_animation, R.anim.fade_in_animation, R.anim.fade_out_animation)
                .add(R.id.container, fragment, "UserProfileFragment")
                .addToBackStack("UserProfileFragment").commitAllowingStateLoss()
    }

    // ==================== Live Change ===============
    override fun OnPrivacyPolicyChanged(title: String, msg: String) {
        ShowPrivacyPolicyFragment(true)
        ShowWarningDialog(title, msg)
    }

    override fun OnEmployeeHasBeenBlocked(title: String, msg: String) {
        ShowSignInFragment()
        ShowWarningDialog(title, msg)
    }
}
