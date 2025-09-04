package com.teknopole.track3rdeye.MVP.Views


import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mvc.imagepicker.ImagePicker
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener
import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.MVP.Contracts.UserProfileFragmentContract
import com.teknopole.track3rdeye.MVP.Presenters.UserProfileFragmentPresenter
import com.teknopole.track3rdeye.ObjectModels.Employee
import com.teknopole.track3rdeye.PopupAndDialogs.ChangePasswordDialog
import com.teknopole.track3rdeye.PopupAndDialogs.MenuPopup
import com.teknopole.track3rdeye.R
import com.teknopole.track3rdeye.Utils.CircularAnimationUtils
import kotlinx.android.synthetic.main.fragment_user_profile.*


class UserProfileFragment : Fragment(), UserProfileFragmentContract.View {
    private val IMAGE_REQUEST_CODE = 22
    private lateinit var presenter: UserProfileFragmentPresenter

    private var changePasswordDialog: ChangePasswordDialog? = null
    private var menuPopup: MenuPopup? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_user_profile, container, false)

        view.addOnLayoutChangeListener(onLayoutChangeListener)
        // Inflate the layout for this fragment
        return view
    }


    private val onLayoutChangeListener = View.OnLayoutChangeListener { v, _, _, _, _, _, _, _, _ ->
        RemoveLayoutChangeListener()
        CircularAnimationUtils().RegisterCircularRevealEnterAnimation(v
                , HomeActivity.touchPosition.x
                , HomeActivity.touchPosition.y
                , v.width, v.height
                , Color.parseColor("#ff7472")
                , Color.WHITE)
    }

    private fun RemoveLayoutChangeListener() {
        view!!.removeOnLayoutChangeListener(onLayoutChangeListener)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter = UserProfileFragmentPresenter(this)

        btnSubmit.visibility = View.GONE
        initMember()
        initEvent()
    }

    override fun onStart() {
        super.onStart()
        presenter.GetUserDetailsOnViewStarted()
    }

    override fun onPause() {
        super.onPause()
        changePasswordDialog?.Close()
        menuPopup?.Close()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == IMAGE_REQUEST_CODE) {
            try {
                val bitmap = ImagePicker.getImageFromResult(context, requestCode, resultCode, data)
                if (bitmap != null) {
                    presenter.OnRequestUploadProfilePic(bitmap)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun initMember() {
        ImageLoader.getInstance().init(app.GetUILConfig())
        ImagePicker.setMinQuality(100, 100)

    }


    private fun initEvent() {
        btnBack.setOnClickListener { activity?.onBackPressed() }
        btnEditPic.setOnClickListener {
            try {
                ImagePicker.pickImage(this, "Select Image Picker", IMAGE_REQUEST_CODE, false)
            } catch (e: Exception) {
                ImagePicker.pickImage(this, "Select Image Picker", IMAGE_REQUEST_CODE, true)
            }
        }
        btnMenu.setOnClickListener {
            menuPopup = MenuPopup(object : MenuPopup.MenuSelectListener {
                override fun OnEditButtonClicked() {
                    listener.OnEditProfileMenuSelected(icCompanyLogo.drawable)
                }

                override fun OnChangePasswordButtonClicked() {
                    changePasswordDialog = ChangePasswordDialog(context!!)
                    changePasswordDialog?.Show()
                }
            })
            menuPopup?.Show(btnMenu)
        }
    }

    //============ Invoked by view ==============
    override fun ShowWarningToast(msg: String) {
        HomeActivity.ShowWarningToast(msg)
    }

    override fun ShowErrorToast(message: String) {
        HomeActivity.ShowErrorToast(message)
    }

    override fun ShowLoadingToast() {
        HomeActivity.StartLoading()
    }

    override fun CompleteLoadingToast(isSuccess: Boolean) {
        HomeActivity.CompleteLoading(isSuccess)
    }


    override fun SetUserDetailsToView(emp: Employee) {
        tvFullName.text = "${emp.firstName} ${emp.lastName}"
        tvDesignation.text = emp.designation

        etFirstName.setText(emp.firstName)
        etLastName.setText(emp.lastName)

        tvDateOfBirth.setText(emp.birthDate)
        tvGender.setText(emp.gender)
        tvUsername.setText(emp.username)
        tvEmail.setText(emp.email)
        tvMobile.setText(emp.mobile)
        tvAddress.setText(emp.address)
    }

    override fun LoadEmployeeProfilePic(imageUrl: String) {
        try {
            icCompanyLogo.setImageBitmap(app.GetImageFromCache("userProfilePicture"))
            progressBar.visibility = View.GONE
            ImageLoader.getInstance()
                    .apply {
                        clearMemoryCache()
                        clearDiskCache()
                        loadImage(imageUrl, object : ImageLoadingListener {
                            override fun onLoadingComplete(imageUri: String?, view: View?, loadedImage: Bitmap?) {
                                try {
                                    if (loadedImage != null) {
                                        icCompanyLogo.setImageBitmap(loadedImage)
                                        app.StoreImageToCache("userProfilePicture", loadedImage!!)
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }

                            override fun onLoadingStarted(imageUri: String?, view: View?) {
                                try {
                                    //  progressBar.visibility =View.VISIBLE
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }

                            override fun onLoadingCancelled(imageUri: String?, view: View?) {
                            }

                            override fun onLoadingFailed(imageUri: String?, view: View?, failReason: FailReason?) {
                                try {
                                    presenter.LoadDefaultImage()
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

    override fun OnUpdateEmployeeSuccess() {

    }

    override fun ChangeUpdateButtonEnabled(enable: Boolean) {

    }

    //============ Event ============
    private lateinit var listener: ActionListener

    fun SetEventListener(listener: ActionListener) {
        this.listener = listener
    }

    interface ActionListener {
        fun OnEditProfileMenuSelected(drawable: Drawable)
    }
}