package com.teknopole.track3rdeye.MVP.Views


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.mvc.imagepicker.ImagePicker
import com.nostra13.universalimageloader.core.ImageLoader
import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.MVP.Adapters.DealerContactPersonListRecyclerAdapter
import com.teknopole.track3rdeye.MVP.Contracts.DealerProfileFragmentContract
import com.teknopole.track3rdeye.MVP.Presenters.DealerProfileFragmentPresenter
import com.teknopole.track3rdeye.ObjectModels.ContactPerson
import com.teknopole.track3rdeye.ObjectModels.DealerObject
import com.teknopole.track3rdeye.PopupAndDialogs.DropDownListPopUp
import com.teknopole.track3rdeye.R
import com.teknopole.track3rdeye.Utils.Convert
import kotlinx.android.synthetic.main.fragment_add_dealer.*
import kotlinx.android.synthetic.main.layout_dealer_info.*


class AddDealerFragment : Fragment(), DealerProfileFragmentContract.View, DealerContactPersonListRecyclerAdapter.EventListener  {
    override fun OnEditContactPersonSuccess() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun ChangeUpdateButtonEnabled(enable: Boolean) {
        btnSubmitDealer.isEnabled = enable
    }

    var dealerId: Int = 0
    var updateDealer = false

    private lateinit var presenter: DealerProfileFragmentPresenter
    private var dropDownLisPopup: DropDownListPopUp? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_add_dealer, container, false)
        dealerId = arguments!!.getInt("dealerId", 0)

        updateDealer = dealerId != 0 //if dealer id is not present that means we are adding a new dealer instead of updating an old older
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter = DealerProfileFragmentPresenter(this)
        // Inflate the layout for this fragment

        EnableEditMode()
        initEvent()

        if (updateDealer)
            presenter.RequestToLoadDealersList(this.dealerId)

    }

    override fun onPause() {
        super.onPause()
        dropDownLisPopup?.Close()
    }

    private fun initEvent() {
        btnBack.setOnClickListener { activity?.onBackPressed() }
        btnSubmitDealer.setOnClickListener {
            presenter.OnEditDealerRequest(GetDealerDetailsFromView(), updateDealer)
        }

        ImageLoader.getInstance().init(app.GetUILConfig())

        pager.adapter = WizardPagerAdapter()
        tabs.setupWithViewPager(pager)
        pager.setCurrentItem(0)
    }

    internal inner class WizardPagerAdapter : PagerAdapter() {

        override fun instantiateItem(collection: ViewGroup, position: Int): Any {
            var resId = 0
            when (position) {
                0 -> resId = R.id.page_one
                1 -> resId = R.id.page_two
            }
            return collection.findViewById(resId)
        }

        override fun getCount(): Int {
            return 2
        }

        override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
            return arg0 === arg1
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            // No super
        }

        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                0 -> return "Dealer"
                1 -> return "Contact Person"
                else -> return ""
            }
        }
    }

    // enable edit mode
    private fun EnableEditMode() {
        val padding = Convert.DpToPixel(8)

        btnMenu.visibility = View.GONE

        tvNameLabel.visibility = View.VISIBLE

        tvName.apply {
            visibility = View.VISIBLE
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
        tvPhone.apply {
            isEnabled = true
            setPadding(padding, padding, padding, padding)
        }
        tvAddress.apply {
            isEnabled = true
            setPadding(padding, padding, padding, padding)
        }

        btnSubmitDealer.visibility = View.VISIBLE
    }


    private val IMAGE_REQUEST_CODE = 22
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == IMAGE_REQUEST_CODE) {
            try {
                val bitmap = ImagePicker.getImageFromResult(context, requestCode, resultCode, data)
                if (bitmap != null) {
                    presenter.OnRequestUploadContactPersonPic(contactPerson, bitmap)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun GetDealerDetailsFromView(): DealerObject {
        val dealer = DealerObject()
        try {
            dealer.companyId = app.GetUserSession().cpProfileId
            dealer.dealerName = tvName.text.toString()
            dealer.dealerEmail = tvEmail.text.toString()
            dealer.dealerMobile = tvMobile.text.toString()
            dealer.dealerPhone = tvPhone.text.toString()
            dealer.dealerAddress = tvAddress.text.toString()

//          if() new
            dealer.createdBy = app.GetUserSession().id
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return dealer
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

    override fun OnEditDealerSuccess() {
        pager.setCurrentItem(1)

        val dealer = GetDealerDetailsFromView()

        //delear contact persons
        rvContactPerson.adapter = DealerContactPersonListRecyclerAdapter(dealer.id,dealer.contactPersons, this)
    }

    override fun SetDealerDetailsToView(dealer: DealerObject) {
        //dealer details
        tvName.setText(dealer.dealerName)
        tvEmail.setText(dealer.dealerEmail)
        tvMobile.setText(dealer.dealerMobile)
        tvAddress.setText(dealer.dealerAddress)
        tvPhone.setText(dealer.dealerPhone)

        //dealer contact persons
        rvContactPerson.adapter = DealerContactPersonListRecyclerAdapter(dealer.id, dealer.contactPersons, this)
    }

    override fun OnContactPersonEditClicked(contactPerson: ContactPerson) {
        presenter.OnRequestEditContactPerson(contactPerson, contactPerson.id!=0)
    }


    private lateinit var contactPerson: ContactPerson

    override fun OnSubmitEditPicClicked(contactPerson: ContactPerson) {

        if (contactPerson.id != 0) {
            this.contactPerson = contactPerson
            try {
                ImagePicker.pickImage(this, "Select Image Picker", IMAGE_REQUEST_CODE, false)
            } catch (e: Exception) {
                ImagePicker.pickImage(this, "Select Image Picker", IMAGE_REQUEST_CODE, true)
            }
        } else {
            Toast.makeText(activity, "Save Contact First", Toast.LENGTH_SHORT).show()
        }

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