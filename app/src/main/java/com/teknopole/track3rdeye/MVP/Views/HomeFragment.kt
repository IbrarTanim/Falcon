package com.teknopole.track3rdeye.MVP.Views

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mancj.slideup.SlideUp
import com.mancj.slideup.SlideUpBuilder
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener
import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.GpsTracking.GpsTrackingService
import com.teknopole.track3rdeye.PopupAndDialogs.ConfirmationDialog
import com.teknopole.track3rdeye.R
import com.teknopole.track3rdeye.Utils.APIClient.RESTClient
import com.teknopole.track3rdeye.Utils.LiveChange
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment(), LiveChange.HomeFragment.LiveChangeListener {


    private var listener: HomeFragmentListener? = null
    private var confirmDialog: ConfirmationDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initEvent()
        LoadCompanyInfo()
    }

    override fun onResume() {
        updateNotificationBadgeCount()
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        confirmDialog?.Close()
    }

    override fun onStart() {
        super.onStart()
        LiveChange.HomeFragment.registerLiveChangeListener(context!!, this)
    }

    override fun onStop() {
        LiveChange.HomeFragment.unregisterLiveChangeListener(context!!)
        super.onStop()
    }

    private var slideUp: SlideUp? = null


    private fun initEvent() {
        btnSetting.setOnClickListener {
            slideUp?.show()
        }
        btnMenu.setOnClickListener {
            listener?.OnNotificationsBellClicked()
        }
        tvNotificationBadge.setOnClickListener {
            listener?.OnNotificationsBellClicked()
        }


        // Main Hompage menu
        btnTask.setOnClickListener {
            listener?.OnTaskButtonClicked()
        }
        btnDealer.setOnClickListener { listener?.OnHistoryButtonClicked() }
        btnOrder.setOnClickListener { listener?.OnOrderButtonClicked() }
        btnProfile.setOnClickListener {

            listener?.OnProfileButtonClicked()
        }


        // Home drawer menu
        switchResumeTracking.setOnToggledListener { _, isOn -> listener?.OnSwitchTrackingToggled(!isOn) }
        btnPrivacyPolicy.setOnClickListener {
            slideUp?.hide()
            Handler().postDelayed({
                listener?.OnDrawerPrivacyPolicyButtonClicked()
            }, 500)
        }
        btnLogout.setOnClickListener {
            slideUp?.hide()
            Handler().postDelayed({
                confirmDialog = ConfirmationDialog(context!!, object : ConfirmationDialog.ClickListener {
                    override fun OnYesButtonClick() {
                        listener?.OnLogoutRequest()
                    }

                    override fun OnNoButtonClick() {
                    }
                })
                if (GpsTrackingService.IsServiceRunning) {
                    confirmDialog?.SetTitleText("YOU ARE BEING TRACKED")
                    confirmDialog?.SetConfirmationText("Do you still want to logout?")
                }
                confirmDialog?.Show()
            }, 300)
        }

        // Init slider drawer
        slideUp = SlideUpBuilder(view?.findViewById(R.id.slidUpDrawer))
                .withListeners(object : SlideUp.Listener.Events {
                    override fun onSlide(percent: Float) {
                        dim.alpha = 1 - percent / 100
                    }

                    override fun onVisibilityChanged(visibility: Int) {
                        dim.isClickable = visibility == View.VISIBLE
                        switchResumeTracking.isOn = !app.IsTrackingPaused()
                        showOrHideTrackingPauseOption()
                    }
                })
                .withStartGravity(Gravity.BOTTOM)
                .withLoggingEnabled(false)
                .withStartState(SlideUp.State.HIDDEN)
                .withSlideFromOtherView(view)
                .withGesturesEnabled(true)
                .build()

    }

    private fun showOrHideTrackingPauseOption() {
        Handler(Looper.getMainLooper()).post {
            try {
                if (app.GetUserSession().canPause == "Yes" && GpsTrackingService.IsServiceRunning) {
                    pausePlayBox.visibility = View.VISIBLE
                } else {
                    pausePlayBox.visibility = View.GONE
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun LoadCompanyInfo() {
        ivCompanyLogo.setImageBitmap(app.GetImageFromCache("companyLogo"))
        tvCompanyName.text = app.GetUserSession().companyName
        try {
            ImageLoader.getInstance()
                    .apply {
                        init(app.GetUILConfig())
                        clearMemoryCache()
                        clearDiskCache()
                        loadImage(RESTClient.GetCompanyImageUrl(app.GetUserSession().companyLogo), object : SimpleImageLoadingListener() {
                            override fun onLoadingComplete(imageUri: String?, view: View?, loadedImage: Bitmap?) {
                                try {
                                    if (loadedImage != null) {
                                        app.StoreImageToCache("companyLogo", loadedImage)
                                        ivCompanyLogo.setImageBitmap(loadedImage)
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        })
                    }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateNotificationBadgeCount() {
        tvNotificationBadge.text = app.GetUnseenNotificationCount().toString()
    }


    // =========== live update ==========
    override fun OnNotificationCountChanged() {
        updateNotificationBadgeCount()
    }

    override fun OnCompanyLogoUpdated() {
        try {
            Handler(Looper.getMainLooper()).post {
                try {
                    LoadCompanyInfo()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun OnTrackingStarted() {
        showOrHideTrackingPauseOption()
    }

    override fun OnTrackingStopped() {
        showOrHideTrackingPauseOption()
    }

    override fun OnSignnedOut() {
        listener?.OnLoggedout();
    }


    // ============ Event ============
    fun SetEventListener(listener: HomeFragmentListener) {
        this.listener = listener
    }

    interface HomeFragmentListener {
        fun OnNotificationsBellClicked()

        fun OnTaskButtonClicked()
        fun OnHistoryButtonClicked()
        fun OnOrderButtonClicked()
        fun OnProfileButtonClicked()

        fun OnLogoutRequest()
        fun OnLoggedout()
        fun OnDrawerPrivacyPolicyButtonClicked()
        fun OnSwitchTrackingToggled(isOn: Boolean)
    }
}