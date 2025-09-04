package com.teknopole.track3rdeye.MVP.Views


import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nostra13.universalimageloader.core.ImageLoader
import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.MVP.Adapters.OrderListRecyclerAdapter
import com.teknopole.track3rdeye.MVP.Contracts.OrderListFragmentContract
import com.teknopole.track3rdeye.MVP.Presenters.OrderListFragmentPresenter
import com.teknopole.track3rdeye.ObjectModels.OrderObject
import com.teknopole.track3rdeye.PopupAndDialogs.MenuTaskFilter
import com.teknopole.track3rdeye.R
import com.teknopole.track3rdeye.Utils.CircularAnimationUtils
import com.teknopole.track3rdeye.Utils.Convert
import com.teknopole.track3rdeye.Utils.LiveChange
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator
import kotlinx.android.synthetic.main.fragment_dealer_list.*
import kotlinx.android.synthetic.main.operation_status_view.*


class OrderListFragment : Fragment(), OrderListFragmentContract.View, LiveChange.OrdersListFragment.LiveChangeListener {
    private lateinit var presenter: OrderListFragmentPresenter
    private var menuTaskFiler: MenuTaskFilter? = null
    var taskFilterBY: String = "Assigned"
    private val orderList = arrayListOf<OrderObject>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_order_list, container, false)

        view.addOnLayoutChangeListener(onLayoutChangeListener)
        // Inflate the layout for this fragment
        return view
    }

    private val onLayoutChangeListener = View.OnLayoutChangeListener { v, _, _, _, _, _, _, _, _ ->
        removeLayoutChangeListener()
        CircularAnimationUtils().RegisterCircularRevealEnterAnimation(v
                , HomeActivity.touchPosition.x
                , HomeActivity.touchPosition.y
                , v.width, v.height
                , Color.parseColor("#ff7472")
                , Color.WHITE)
    }

    private fun removeLayoutChangeListener() {
        view!!.removeOnLayoutChangeListener(onLayoutChangeListener)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter = OrderListFragmentPresenter(context!!, this)
        initMember()
        initEvent()
    }

    override fun onPause() {
        super.onPause()
        menuTaskFiler?.Close()
    }

    override fun onStart() {
        super.onStart()
        LiveChange.OrdersListFragment.registerLiveChangeListener(context!!, this)
    }

    override fun onStop() {
        super.onStop()
        LiveChange.OrdersListFragment.unregisterLiveChangeListener(context!!)
    }


    private fun initMember() {
        swipeLayout.setColorSchemeResources(R.color.colorLightPink)
        swipeLayout.setSize(Convert.DpToPixel(30))
        ImageLoader.getInstance().init(app.GetUILConfig())
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = orderAdapter
        recyclerView.itemAnimator = SlideInDownAnimator()

    }

    private fun initEvent() {
        btnBack.setOnClickListener { activity?.onBackPressed() }
        btnMenu.setOnClickListener {
            menuTaskFiler = MenuTaskFilter(context!!, taskFilterBY, object : MenuTaskFilter.MenuSelectListener {
                override fun FilterBy(filterBy: String) {
                    taskFilterBY = filterBy
                    presenter.RequestToLoadOrderList(taskFilterBY)
                }
            })
            menuTaskFiler?.Show(it)
        }
        swipeLayout.setOnRefreshListener {
            presenter.RequestRefreshOrderList(taskFilterBY)
        }
        fab.setOnClickListener {
            listener.OnFabClickedShowAddNewOrderFragment()
        }
        //  btnLoadMore.setOnClickListener { }

        // first time load
        presenter.RequestToLoadOrderList(taskFilterBY)
    }


    // task adapter
    private val orderAdapter = OrderListRecyclerAdapter(orderList, object : OrderListRecyclerAdapter.EventListener {
        override fun OnScrollDown() {
            onCollapseFab()
        }

        override fun OnScrollUp() {
            onExpandFab()
        }


        override fun OnItemClicked(order: OrderObject) {
            listener.OnOrderItemSelectedShowOrderDetailsFragment(order)
        }

        override fun OnScrolledToBottom() {
            // presenter.OnScrolledToBottom()
        }
    })

    fun RequestRefreshing() {
        presenter.RequestRefreshOrderList(taskFilterBY)
    }


    //============ Invoked by view ==============
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

    fun onExpandFab() {
        fab.fabTextVisibility = View.VISIBLE;
    }

    fun onCollapseFab() {
        fab.fabTextVisibility = View.GONE
    }

    override fun ShowLoadMoreButton() {
        Handler(Looper.getMainLooper()).post {
            try {
                btnLoadMore.visibility = View.VISIBLE
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun HideLoadMoreButton() {
        Handler(Looper.getMainLooper()).post {
            try {
                btnLoadMore.visibility = View.GONE
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun ShowLoadMoreProgress() {
        Handler(Looper.getMainLooper()).post {
            try {
                progressBar.visibility = View.VISIBLE
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun HideLoadMoreProgress() {
        Handler(Looper.getMainLooper()).post {
            try {
                progressBar.visibility = View.GONE
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun ShowEmptyView() {
        Handler(Looper.getMainLooper()).post {
            try {
                OperationStatusView.visibility = View.VISIBLE
                tvOperationStatusText.text = "There are no items to show"
                ivOperationStatusIcon.setImageResource(R.drawable.ic_empty_box)
                tvFilterText.visibility = View.GONE
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
                tvFilterText.visibility = View.GONE
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun HideEmptyView() {
        Handler(Looper.getMainLooper()).post {
            try {
                OperationStatusView.visibility = View.GONE
                tvFilterText.visibility = View.VISIBLE
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun SetFilterText() {
        Handler(Looper.getMainLooper()).post {
            try {
                tvFilterText.text = "Showing all $taskFilterBY task"
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun ClearOrderList() {
        Handler(Looper.getMainLooper()).post {
            try {
                orderList.clear()
                orderAdapter.notifyDataSetChanged()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun AddOrderToRecyclerView(task: OrderObject) {
        Handler(Looper.getMainLooper()).post {
            try {
                orderList.add(task)
                orderAdapter.notifyItemInserted(orderList.size - 1)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    //============ Live update ===========
    override fun OnNewOrderAdded() {
        presenter.RequestRefreshOrderList("Assigned")
    }

    override fun OnOrderUpdated(taskId: Int) {
        presenter.RequestRefreshOrderList(taskFilterBY)
    }

    override fun OnOrderDescriptionAdded(taskId: Int) {
        presenter.RequestRefreshOrderList(taskFilterBY)
    }

    override fun OnOrderAttachmentAdded(taskId: Int) {
        presenter.RequestRefreshOrderList(taskFilterBY)
    }

    override fun OnOrderAttachmentDeleted(taskId: Int) {
        presenter.RequestRefreshOrderList(taskFilterBY)
    }

    override fun OnOrderMemberAdded(taskId: Int) {
        presenter.RequestRefreshOrderList(taskFilterBY)
    }

    override fun OnOrderMemberRemoved(taskId: Int) {
        presenter.RequestRefreshOrderList(taskFilterBY)
    }


    //============ Event ============
    private lateinit var listener: ActionListener

    fun SetEventListener(listener: ActionListener) {
        this.listener = listener
    }

    interface ActionListener {
        fun OnOrderItemSelectedShowOrderDetailsFragment(task: OrderObject)
        fun OnFabClickedShowAddNewOrderFragment()
    }
}