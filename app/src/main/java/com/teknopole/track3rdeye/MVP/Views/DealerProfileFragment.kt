package com.teknopole.track3rdeye.MVP.Views


import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nostra13.universalimageloader.core.ImageLoader
import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.MVP.Adapters.DealerContactPersonListRecyclerAdapter
import com.teknopole.track3rdeye.MVP.Contracts.DealerProfileFragmentContract
import com.teknopole.track3rdeye.MVP.Presenters.DealerProfileFragmentPresenter
import com.teknopole.track3rdeye.ObjectModels.DealerObject
import com.teknopole.track3rdeye.PopupAndDialogs.MenuPopup
import com.teknopole.track3rdeye.R
import com.teknopole.track3rdeye.Utils.CircularAnimationUtils
import kotlinx.android.synthetic.main.fragment_dealer_profile.*
import kotlinx.android.synthetic.main.layout_dealer_info.*


class DealerProfileFragment : Fragment(), DealerProfileFragmentContract.View {



    private val IMAGE_REQUEST_CODE = 22
    private lateinit var presenter: DealerProfileFragmentPresenter

    private var menuPopup: MenuPopup? = null

    var dealerId: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_dealer_profile, container, false)

        view.addOnLayoutChangeListener(onLayoutChangeListener)
        dealerId = arguments!!.getInt("dealerId")
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
        presenter = DealerProfileFragmentPresenter(this)

        btnSubmitDealer.visibility = View.GONE
        initMember()
        initEvent()

        presenter.RequestToLoadDealersList(this.dealerId)
    }

    override fun onPause() {
        super.onPause()
        menuPopup?.Close()
    }

    override fun OnEditContactPersonSuccess() {

    }

    private fun initMember() {
        ImageLoader.getInstance().init(app.GetUILConfig())

        pager.adapter = WizardPagerAdapter()
    }


    private fun initEvent() {
        btnBack.setOnClickListener { activity?.onBackPressed() }
        btnMenu.setOnClickListener {
            menuPopup = MenuPopup(object : MenuPopup.MenuSelectListener {
                override fun OnChangePasswordButtonClicked() {

                }

                override fun OnEditButtonClicked() {
                    listener.OnEditDealerSelected(dealerId)
                }

            })
            menuPopup?.Show(btnMenu)
        }
        tabs.setupWithViewPager(pager)
        pager.setCurrentItem(0)
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


    override fun SetDealerDetailsToView(dealer: DealerObject) {
        //dealer details
        tvDealerName.setText(dealer.dealerName)
        tvEmail.setText(dealer.dealerEmail)
        tvMobile.setText(dealer.dealerMobile)
        tvPhone.setText(dealer.dealerPhone)
        tvAddress.setText(dealer.dealerAddress)

        //delear contact persons
        rvContactPerson.adapter = DealerContactPersonListRecyclerAdapter(dealer.id, dealer.contactPersons, null)
    }

    internal inner class WizardPagerAdapter() : PagerAdapter() {

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

    override fun OnEditDealerSuccess() {

    }
    override fun ChangeUpdateButtonEnabled(enable: Boolean) {

    }
    //============ Event ============
    private lateinit var listener: ActionListener

    fun SetEventListener(listener: ActionListener) {
        this.listener = listener
    }

    interface ActionListener {
        fun OnEditDealerSelected(dealer: Int)
    }
}