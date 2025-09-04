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
import com.teknopole.track3rdeye.MVP.Adapters.NotificationListRecyclerAdapter
import com.teknopole.track3rdeye.MVP.Contracts.NotificationsFragmentContract
import com.teknopole.track3rdeye.MVP.Presenters.NotificationsFragmentPresenter
import com.teknopole.track3rdeye.ObjectModels.NotificationObject
import com.teknopole.track3rdeye.PopupAndDialogs.NotificationDetailsPopup
import com.teknopole.track3rdeye.R
import com.teknopole.track3rdeye.Utils.CircularAnimationUtils
import com.teknopole.track3rdeye.Utils.Convert
import com.teknopole.track3rdeye.Utils.LiveChange
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.android.synthetic.main.operation_status_view.*


class NotificationsFragment : Fragment(), NotificationsFragmentContract.View, LiveChange.NotificationsFragment.LiveChangeListener {

    private lateinit var presenter: NotificationsFragmentPresenter
    private var notificationDetailsPopup: NotificationDetailsPopup? = null

    private val notificationList = arrayListOf<NotificationObject>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_notifications, container, false)

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
        presenter = NotificationsFragmentPresenter(context!!, this)
        initMember()
        initEvent()
    }


    private fun initMember() {
        swipeLayout.setColorSchemeResources(R.color.colorLightPink)
        swipeLayout.setSize(Convert.DpToPixel(30))
        ImageLoader.getInstance().init(app.GetUILConfig())
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = notificationsAdapter
        //  recyclerView.itemAnimator =
    }

    private fun initEvent() {

        btnBack.setOnClickListener { activity?.onBackPressed() }
//        btnMenu.setOnClickListener {
////            MenuTaskFilter(context!!,object :MenuTaskFilter.MenuSelectListener{
////
////
////            }).Show(it)
//        }

        swipeLayout.setOnRefreshListener {
            presenter.RequestRefreshList()
        }
//        recyclerView.addOnScrollListener(object :RecyclerViewScrollListener(){
//            override fun onScrolledToBottom() {
//                presenter.RequestLoadMore()
//            }
//        })
    }


    override fun onResume() {
        super.onResume()
        presenter.RequestToLoadList()
    }

    override fun onPause() {
        notificationDetailsPopup?.Close()
        super.onPause()
    }

    override fun onStart() {
        super.onStart()
        LiveChange.NotificationsFragment.registerLiveChangeListener(context!!, this)
    }

    override fun onStop() {
        LiveChange.NotificationsFragment.unregisterLiveChangeListener(context!!)
        super.onStop()
    }


    private val notificationsAdapter = NotificationListRecyclerAdapter(notificationList, object : NotificationListRecyclerAdapter.ItemClickListener {
        override fun OnItemClicked(notification: NotificationObject) {
            notificationDetailsPopup = NotificationDetailsPopup(notification)
            notificationDetailsPopup?.Show(view!!)
            presenter.MarkNotificationAsSeen(notification)
        }
    })


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

    override fun ClearList() {
        Handler(Looper.getMainLooper()).post {
            try {
                notificationList.clear()
                notificationsAdapter.notifyDataSetChanged()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun AddNotificationToRecyclerView(notification: NotificationObject) {
        Handler(Looper.getMainLooper()).post {
            try {
                notificationList.add(notification)
                notificationsAdapter.notifyItemInserted(notificationList.size - 1)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun AddNotificationsToRecyclerView(notificationList: ArrayList<NotificationObject>) {
        Handler(Looper.getMainLooper()).post {
            try {
                this.notificationList.addAll(notificationList)
                notificationsAdapter.notifyDataSetChanged()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    // =========== live update ==========
    override fun OnNotificationCountChanged() {
        presenter.RequestRefreshList()
    }


    //============ Event ============
    private lateinit var listener: ActionListener

    fun SetEventListener(listener: ActionListener) {
        this.listener = listener
    }

    interface ActionListener
}