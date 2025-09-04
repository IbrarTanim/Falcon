package com.teknopole.track3rdeye.MVP.Views


import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.MVP.Contracts.UserProfileFragmentContract
import com.teknopole.track3rdeye.MVP.Presenters.UserProfileFragmentPresenter
import com.teknopole.track3rdeye.ObjectModels.Employee
import com.teknopole.track3rdeye.PopupAndDialogs.DatePickerDialog
import com.teknopole.track3rdeye.PopupAndDialogs.DropDownListPopUp
import com.teknopole.track3rdeye.R
import com.teknopole.track3rdeye.Utils.Convert
import kotlinx.android.synthetic.main.fragment_user_profile.*


class UpdateUserProfileFragment : Fragment(), UserProfileFragmentContract.View {
    lateinit var imageDrawable: Drawable
    private lateinit var presenter: UserProfileFragmentPresenter
    private var datePickerDialog: DatePickerDialog? = null
    private var dropDownLisPopup: DropDownListPopUp? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_user_profile, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter = UserProfileFragmentPresenter(this)
        // Inflate the layout for this fragment

        icCompanyLogo.setImageDrawable(imageDrawable)
        EnableEditMode()
        initEvent()
    }

    override fun onStart() {
        super.onStart()
        presenter.GetUserDetailsOnViewStarted()
    }

    override fun onPause() {
        super.onPause()
        datePickerDialog?.Close()
        dropDownLisPopup?.Close()
    }

    private fun initEvent() {
        btnBack.setOnClickListener { activity?.onBackPressed() }
        btnSubmit.setOnClickListener {
            presenter.OnUpdateButtonClicked(GetUserDetailsFromView())
        }
        tvDateOfBirth.setOnClickListener {
            val date = tvDateOfBirth.text.toString()

            datePickerDialog = DatePickerDialog(context!!, date.substring(0, 2).toInt(), date.substring(3, 5).toInt(), date.substring(6).toInt(), object : DatePickerDialog.ClickListener {
                override fun onDateSelected(stringDate: String) {
                    tvDateOfBirth.setText(stringDate)
                }
            })
            datePickerDialog?.SetButtonText("Set birth date")
            datePickerDialog?.BackgroundDim(false)
            datePickerDialog?.Show()
        }
        tvGender.setOnClickListener {
            dropDownLisPopup = DropDownListPopUp(context!!, arrayListOf("Male", "Female"), object : DropDownListPopUp.ItemSelectListener {
                override fun OnItemSelected(actionText: String) {
                    tvGender.setText(actionText)
                }
            })
            dropDownLisPopup?.Show(tvGender)
        }
    }


    // enable edit mode
    private fun EnableEditMode() {
        val padding = Convert.DpToPixel(8)
        progressBar.visibility = View.GONE
        tvFullName.visibility = View.GONE
        btnMenu.visibility = View.GONE
        btnEditPic.visibility = View.GONE

        tvFirstNameLabel.visibility = View.VISIBLE
        etFirstName.apply {
            visibility = View.VISIBLE
            setPadding(padding, padding, padding, padding)
        }
        tvLastNameLabel.visibility = View.VISIBLE
        etLastName.apply {
            visibility = View.VISIBLE
            setPadding(padding, padding, padding, padding)
        }

        tvDateOfBirth.apply {
            isEnabled = true
            setPadding(padding, padding, padding, padding)
        }
        tvGender.apply {
            isEnabled = true
            setPadding(padding, padding, padding, padding)
            setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down, 0)
        }
        tvUsername.apply {
            isEnabled = true
            setPadding(padding, padding, padding, padding)
        }
        tvEmail.apply {
            isEnabled = true
            setPadding(padding, padding, padding, padding)
        }
        tvMobile.apply {
            isEnabled = true
            setPadding(padding, padding, padding, padding)
        }
        tvAddress.apply {
            isEnabled = true
            setPadding(padding, padding, padding, padding)
        }

        btnSubmit.visibility = View.VISIBLE
    }

    private fun GetUserDetailsFromView(): Employee {
        val employee = app.GetUserSession()
        try {
            employee.firstName = etFirstName.text.toString()
            employee.lastName = etLastName.text.toString()
            employee.birthDate = tvDateOfBirth.text.toString()
            employee.gender = tvGender.text.toString()
            employee.username = tvUsername.text.toString()
            employee.email = tvEmail.text.toString()
            employee.mobile = tvMobile.text.toString()
            employee.address = tvAddress.text.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return employee
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


    override fun ChangeUpdateButtonEnabled(enable: Boolean) {
        btnSubmit.isEnabled = enable
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
    }


    override fun OnUpdateEmployeeSuccess() {
        listener.OnUpdateEmployeeSuccess()
    }


    //============ Event ============
    private lateinit var listener: ActionListener

    fun SetEventListener(listener: ActionListener) {
        this.listener = listener
    }

    interface ActionListener {
        fun OnUpdateEmployeeSuccess()
    }
}