package com.teknopole.track3rdeye.MVP.Views


import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.teknopole.track3rdeye.MVP.Adapters.OrderProductListRecyclerAdapter

import com.teknopole.track3rdeye.MVP.Contracts.OrderDetailsFragmentContract
import com.teknopole.track3rdeye.MVP.Presenters.OrderDetailsFragmentPresenter
import com.teknopole.track3rdeye.ObjectModels.OrderObject
import com.teknopole.track3rdeye.ObjectModels.ProductObject
import com.teknopole.track3rdeye.PopupAndDialogs.*

import com.teknopole.track3rdeye.R
import com.teknopole.track3rdeye.Utils.CircularAnimationUtils
import com.teknopole.track3rdeye.Utils.Convert
import com.teknopole.track3rdeye.Utils.LiveChange
import kotlinx.android.synthetic.main.fragment_order_details.*
import kotlinx.android.synthetic.main.operation_status_view.*
import kotlinx.android.synthetic.main.template_dealer_list.*


class OrderDetailsFragment : Fragment(), OrderDetailsFragmentContract.View, LiveChange.OrderDetailsFragment.LiveChangeListener {

    private lateinit var presenter: OrderDetailsFragmentPresenter
    private var instanceState: Bundle? = null
    var orderId: Int = 0
    private var menuPopup: MenuPopup? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        instanceState = savedInstanceState
        val view = inflater.inflate(R.layout.fragment_order_details, container, false)

        view.addOnLayoutChangeListener(onLayoutChangeListener)
        orderId = arguments!!.getInt("orderId")
        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        instanceState = savedInstanceState
        presenter = OrderDetailsFragmentPresenter(context!!, this)
        initMember()
        initEvent()

        // load task details on created
        presenter.RequestToLoadOrderDetails(this.orderId)
    }

    fun GetOrderDetails() {
        StartSwipeLoading()


        Handler().postDelayed({
            try {
                presenter.RequestToLoadOrderDetails(this.orderId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, 100)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStart() {
        super.onStart()
        LiveChange.OrderDetailsFragment.registerLiveChangeListener(context!!, this)
    }

    override fun onStop() {
        super.onStop()
        LiveChange.OrderDetailsFragment.unregisterLiveChangeListener(context!!)
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


    private fun initMember() {
        swipeLayout.setColorSchemeResources(R.color.colorLightPink)
    }

    private fun initEvent() {
        btnBack.setOnClickListener { activity?.onBackPressed() }
        btnSubmit.setOnClickListener { SubmitOrder() }
        swipeLayout.setOnRefreshListener { presenter.RequestRefreshOrderDetails(orderId) }
        btnMenu.setOnClickListener {
            menuPopup = MenuPopup(object : MenuPopup.MenuSelectListener {
                override fun OnChangePasswordButtonClicked() {

                }

                override fun OnEditButtonClicked() {
                    listener.OnEditOrderSelected(orderId)
                }


            })
            menuPopup?.Show(btnMenu)
        }

    }
    override fun SetOrderDetailsToView(order: OrderObject) {

        //order segment
        tvOrderId.text = order.orderCode
        tvOrderTitle.text = "Grand Total: ${order.grandTotalAmount}"
        tvCreatedTime.text = Convert.FormatDateTime(order.createdOn, "dd MMM, yyyy")
        tvOrderStatus.text = order.orderStatus

        //dealer segment
        tvDealerName.text = order.dealerName
        tvDealerAddress.text = order.dealerAddress
        tvDealerMobile.text = order.dealerMobile
        tvDealerPhone.text = order.dealerPhone

        if (order.dealerPhone.isEmpty()) {
            tvDealerPhone.visibility = View.GONE
        }

        //product segment
        rvProduct.adapter = OrderProductListRecyclerAdapter(order.productDetails, object : OrderProductListRecyclerAdapter.EventListener{
            override fun OnItemClicked(product: ProductObject) {

            }
        })


    }


    private fun SubmitOrder() {
//        confirmDialog = ConfirmationDialog(context!!, object : ConfirmationDialog.ClickListener {
//            override fun OnYesButtonClick() {
//                presenter.OnSubmitButtonClicked(btnSubmit.tag as Boolean)
//            }
//
//            override fun OnNoButtonClick() {
//
//            }
//        }).apply {
//            if (btnSubmit.tag as Boolean) {
//                SetConfirmationText("Do you really want to start this task. If you start task, You will not be able to undo.")
//            } else {
//                SetConfirmationText("Do you really want to complete this task. If you complete, You will not be able to undo.")
//            }
//            BackgroundDim(false)
//        }
//        confirmDialog?.Show()
    }


    //============ Invoked by presenter =============
    override fun StartSwipeLoading() {
        Handler(Looper.getMainLooper()).post {
            try {
                swipeLayout.isRefreshing = true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun StopSwipLoading() {
        Handler(Looper.getMainLooper()).post {
            try {
                swipeLayout.isRefreshing = false
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    override fun ShowEmptyView() {
        Handler(Looper.getMainLooper()).post {
            try {
                OperationStatusView.visibility = View.VISIBLE
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun ShowStatusError(message: String) {
        Handler(Looper.getMainLooper()).post {
            try {
                OperationStatusView.visibility = View.VISIBLE
                tvOperationStatusText.text = message
                ivOperationStatusIcon.setImageResource(R.drawable.ic_status_error)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun HideEmptyView() {
        Handler(Looper.getMainLooper()).post {
            try {
                OperationStatusView.visibility = View.GONE
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    // show or hide segment
    override fun DisableSwipeLoading() {
        Handler(Looper.getMainLooper()).post {
            try {
                swipeLayout.isEnabled = false
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //=========== Live update ===========
    override fun OnOrderUpdated(orderId: Int) {
        if (this.orderId == orderId) {
            presenter.OnOrderUpdated(orderId)
        }
    }

    override fun OnOrderDescriptionAdded(orderId: Int) {
        if (this.orderId == orderId) {
            presenter.OnOrderUpdated(orderId)
        }
    }

    override fun OnOrderAttachmentAdded(orderId: Int) {
        if (this.orderId == orderId) {
            presenter.OnOrderUpdated(orderId)
        }
    }

    override fun OnOrderAttachmentDeleted(orderId: Int) {
        if (this.orderId == orderId) {
            presenter.OnOrderUpdated(orderId)
        }
    }

    override fun OnOrderMemberAdded(orderId: Int) {
        if (this.orderId == orderId) {
            presenter.OnOrderUpdated(orderId)
        }
    }

    override fun OnOrderMemberRemoved(orderId: Int) {
        if (this.orderId == orderId) {
            presenter.OnOrderUpdated(orderId)
        }
    }


    //============ Event ============
    private lateinit var listener: ActionListener

    fun SetEventListener(listener: ActionListener) {
        this.listener = listener
    }

    interface ActionListener {
        fun OnSubmitOrder()
        fun OnShowProductDetailsFragment()
        fun OnShowDealerDetailsFragment()
        fun OnEditOrderSelected(orderId: Int)

    }
}