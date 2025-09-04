package com.teknopole.track3rdeye.MVP.Views


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.MVP.Adapters.ProductListRecyclerAdapter
import com.teknopole.track3rdeye.MVP.Contracts.AddOrderFragmentContract
import com.teknopole.track3rdeye.MVP.Presenters.AddOrderFragmentPresenter
import com.teknopole.track3rdeye.ObjectModels.DealerObject
import com.teknopole.track3rdeye.ObjectModels.OrderObject
import com.teknopole.track3rdeye.ObjectModels.OrderProductDetail
import com.teknopole.track3rdeye.ObjectModels.ProductObject
import com.teknopole.track3rdeye.R
import com.teknopole.track3rdeye.Utils.Convert
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator
import kotlinx.android.synthetic.main.fragment_add_order.*


class AddOrderFragment : Fragment(), AddOrderFragmentContract.View {


    var orderId: Int = 0
    var updateOrder= false

    private lateinit var presenter: AddOrderFragmentPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_add_order, container, false)

        orderId = arguments!!.getInt("orderId")
        updateOrder = orderId != 0 //if dealer id is not present that means we are adding a new dealer instead of updating an old older
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter = AddOrderFragmentPresenter(this)
        // Inflate the layout for this fragment

        initMembers()
        initEvent()

        presenter.RequestToLoadDealerList()
        presenter.RequestToLoadProductList()


        if (updateOrder)
            presenter.RequestToLoadOrderDetails(this.orderId)
    }

    private fun initEvent() {
        btnBack.setOnClickListener { activity?.onBackPressed() }
        btnSubmit.setOnClickListener {
                        presenter.OnSubmitButtonClicked(GetOrderDetailsFromView(), updateOrder)
        }
        etDealer.setOnItemClickListener { parent, view, position, id ->
            btnDealerInfo.visibility = View.VISIBLE
            val selectedDealer = parent.getAdapter().getItem(position) as DealerObject
            presenter.onDealerSelected(selectedDealer)
        }

        etDealer.setOnTouchListener { v, event ->
            etDealer.showDropDown()//show all items on touch
            false
        }

        btnDealerInfo.setOnClickListener {
            listener.OnDealerInfoButtonClicked(presenter.selectedDealer)
        }

        etProduct.setOnItemClickListener { parent, view, position, id ->
            val product = parent.getAdapter().getItem(position) as ProductObject
            presenter.OnProductAdded(product)
        }

        etProduct.setOnTouchListener { v, event ->
            etProduct.showDropDown()//show all items on touch
            false
        }

        rvProduct.adapter =  ProductListRecyclerAdapter(presenter.addedProductList, object : ProductListRecyclerAdapter.EventListener {
            override fun onAmountChanged() {
                var total = 0;
                for (i in presenter.addedProductList) {
                    total = (total + i.totalAmount).toInt()
                }
                etGrandTotal.text = "" + total
            }

            override fun OnItemDeleted(position: Int) {
             presenter.OnProductRemoved(position)
            }

            override fun OnItemClicked(product: OrderProductDetail) {
                listener.OnProductInfoButtonClicked(product.productId)
            }
        })
        rvProduct.itemAnimator = SlideInDownAnimator()
    }


    private fun initMembers() {
        val padding = Convert.DpToPixel(8)
        btnMenu.visibility = View.GONE
        btnDealerInfo.visibility = View.INVISIBLE

        tvDealerLabel.visibility = View.VISIBLE
        etDealer.apply {
            visibility = View.VISIBLE
            setPadding(padding, padding, padding, padding)
        }

        tvProductLabel.visibility = View.VISIBLE
        etProduct.apply {
            visibility = View.VISIBLE
            setPadding(padding, padding, padding, padding)
        }



        btnSubmit.visibility = View.VISIBLE
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



    private fun GetOrderDetailsFromView(): OrderObject
    {
        val order = order
            try {
                order.companyId = app.GetUserSession().cpProfileId
                order.employeeId = app.GetUserSession().id
                order.orderStatus = "Pending"

                order.dealerId = presenter.selectedDealer.id
                order.dealerName = presenter.selectedDealer.dealerName
                order.dealerEmail = presenter.selectedDealer.dealerEmail
                order.dealerAddress = presenter.selectedDealer.dealerAddress
                order.dealerMobile = presenter.selectedDealer.dealerMobile
                order.dealerPhone = presenter.selectedDealer.dealerPhone

                order.employeeDesignation = app.GetUserSession().designation
                order.employeeFullName= app.GetUserSession().designation
                order.employeeFirstName= app.GetUserSession().designation
                order.employeeLastName= app.GetUserSession().designation
                order.employeePhoto= app.GetUserSession().designation
                order.employeeUsername= app.GetUserSession().designation

                order.productDetails = presenter.addedProductList
                order.grandTotalAmount = etGrandTotal.text.toString().toDouble()
            }
            catch (e:Exception) {
                e.printStackTrace()
            }
        return order
    }

    override fun ChangeSubmitButtonEnabled(enable: Boolean) {
        btnSubmit.isEnabled = enable
    }

    override fun SetDealerListToView(dealerList: List<DealerObject>) {
        val adapter = ArrayAdapter<DealerObject>(activity, android.R.layout.simple_spinner_dropdown_item, dealerList)
        etDealer.setAdapter(adapter)
    }

    override fun SetProductListToView(productList: List<ProductObject>) {
        val adapter = ArrayAdapter<ProductObject>(activity, android.R.layout.simple_spinner_dropdown_item, productList)
        etProduct.setAdapter(adapter)
    }


    override fun AddProductInfoToView(product: ProductObject) {
        Handler(Looper.getMainLooper()).post {
            try {
                rvProduct.adapter.notifyItemInserted(presenter.addedProductList.size - 1)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun RemoveProductInfoFromView(position: Int) {
        Handler(Looper.getMainLooper()).post {
            try {
                rvProduct.adapter.notifyItemRemoved(position)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    var order = OrderObject()

    override fun SetOrderDetailsToView(order: OrderObject) {
        this.order = order
        //dealer details
        etDealer.setText(order.dealerName)

        presenter.selectedDealer = presenter.dealerList.filter { d -> d.id == order.employeeId }[0]

        presenter.selectedDealer.id = order.dealerId
                presenter.selectedDealer.dealerName= order.dealerName
                presenter.selectedDealer.dealerEmail= order.dealerEmail
                presenter.selectedDealer.dealerAddress=   order.dealerAddress
                presenter.selectedDealer.dealerMobile =order.dealerMobile
                presenter.selectedDealer.dealerPhone=  order.dealerPhone


        presenter.addedProductList.addAll(order.productDetails)
        rvProduct.adapter.notifyDataSetChanged()

        var total = 0;
        for (i in presenter.addedProductList) {
            total = (total + i.totalAmount).toInt()
        }
        etGrandTotal.text = "" + total
    }

    override fun OnAddOrderSuccess() {
        listener.OnAddOrderSuccess()
    }


    //============ Event ============
    private lateinit var listener: ActionListener

    fun SetEventListener(listener: ActionListener) {
        this.listener = listener
    }

    interface ActionListener {
        fun OnAddOrderSuccess()
        fun OnDealerInfoButtonClicked(order: DealerObject)
        fun OnProductInfoButtonClicked(product: Int)

    }


    //============ Event ============

}