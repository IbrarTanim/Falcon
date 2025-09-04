package com.teknopole.track3rdeye.MVP.Views


import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.teknopole.track3rdeye.MVP.Adapters.ProductDetailsListRecyclerAdapter
import com.teknopole.track3rdeye.MVP.Adapters.ProductImagesGridRecyclerAdapter
import com.teknopole.track3rdeye.MVP.Contracts.ProductDetailsFragmentContract
import com.teknopole.track3rdeye.MVP.Presenters.ProductDetailsFragmentPresenter
import com.teknopole.track3rdeye.ObjectModels.ProductObject
import com.teknopole.track3rdeye.R
import com.teknopole.track3rdeye.Utils.CircularAnimationUtils
import com.teknopole.track3rdeye.Utils.LiveChange
import kotlinx.android.synthetic.main.fragment_product_details.*
import kotlinx.android.synthetic.main.operation_status_view.*
import kotlinx.android.synthetic.main.template_product_profile_details.*
import kotlinx.android.synthetic.main.template_product_profile_images.*
import kotlinx.android.synthetic.main.layout_product_profile_info.*


class ProductDetailsFragment : Fragment(), ProductDetailsFragmentContract.View, LiveChange.ProductDetailsFragment.LiveChangeListener {


    private lateinit var presenter: ProductDetailsFragmentPresenter
    private var instanceState: Bundle? = null

    var productId: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        instanceState = savedInstanceState
        val view = inflater.inflate(R.layout.fragment_product_details, container, false)

        view.addOnLayoutChangeListener(onLayoutChangeListener)
        productId = arguments!!.getInt("productId")
        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        instanceState = savedInstanceState
        presenter = ProductDetailsFragmentPresenter(context!!, this)
        initMember()
        initEvent()

        // load task details on created
        presenter.RequestToLoadProductDetails(this.productId)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStart() {
        super.onStart()
        LiveChange.ProductDetailsFragment.registerLiveChangeListener(context!!, this)
    }

    override fun onStop() {
        super.onStop()
        LiveChange.ProductDetailsFragment.unregisterLiveChangeListener(context!!)
    }

    override fun onDestroy() {
        super.onDestroy()
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
        pager.offscreenPageLimit = 2
        pager.adapter = WizardPagerAdapter()

    }

    private fun initEvent() {
        btnBack.setOnClickListener { activity?.onBackPressed() }
        tabs.setupWithViewPager(pager)
        pager.setCurrentItem(0)
    }

    internal inner class WizardPagerAdapter() : PagerAdapter() {

        override fun instantiateItem(collection: ViewGroup, position: Int): Any {
            var resId = 0
            when (position) {
                0 -> resId = R.id.page_one
                1 -> resId = R.id.page_two
                2 -> resId = R.id.page_three
            }
            return collection.findViewById(resId)
        }

        override fun getCount(): Int {
            return 3
        }

        override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
            return arg0 === arg1
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            // No super
        }

        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                0 -> return "Info"
                1 -> return "Details"
                2 -> return "Images"
                else -> return ""
            }
        }
    }
    //============ Invoked by presenter =============
    override fun StartSwipeLoading() {
        Handler(Looper.getMainLooper()).post {
            try {
                swipeLayout.isIndeterminate  = true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun StopSwipLoading() {
        Handler(Looper.getMainLooper()).post {
            try {
                swipeLayout.isIndeterminate = false
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

    override fun ShowProductSegment(product: ProductObject) {

//title
        tvProductName.text = product.productName

//first page
        tvProductCode.text = product.productCode
        tvCurrentStock.text = product.currentStockQty.toString()
        tvSalesPrice.text = product.currentSalesPrice.toString()

        //2nd page
        rvDetails.adapter = ProductDetailsListRecyclerAdapter(product.productDetails)


        //3rd page
        rvImages.adapter = ProductImagesGridRecyclerAdapter(product.productImages)
    }

    //=========== Live update ===========


    //============ Event ============
    private lateinit var listener: ActionListener

    fun SetEventListener(listener: ActionListener) {
        this.listener = listener
    }

    interface ActionListener {



    }
}