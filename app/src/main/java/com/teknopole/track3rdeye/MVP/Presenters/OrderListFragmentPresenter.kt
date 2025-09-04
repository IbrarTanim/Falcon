package com.teknopole.track3rdeye.MVP.Presenters

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.MVP.Contracts.OrderListFragmentContract
import com.teknopole.track3rdeye.MVP.DataModels.OrderListFragmentDataModel
import com.teknopole.track3rdeye.MVP.Views.HomeActivity
import com.teknopole.track3rdeye.ObjectModels.OrderObject
import com.teknopole.track3rdeye.Utils.APIClient.Error

class OrderListFragmentPresenter(private val context: Context, private val view: OrderListFragmentContract.View) : OrderListFragmentContract.Presenter {
    val dataModel = OrderListFragmentDataModel(this)


    override fun RequestToLoadOrderList(taskFilterBY: String) {
        Thread {
            try {
                view.StartSwipeLoading()
                view.HideLoadMoreProgress()
                view.HideLoadMoreButton()

                GetOrderListWithFilter(taskFilterBY)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    override fun RequestRefreshOrderList(taskFilterBY: String) {
        Thread {
            try {
                view.HideLoadMoreProgress()
                view.HideLoadMoreButton()

                GetOrderListWithFilter(taskFilterBY)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun GetOrderListWithFilter(taskFilterBY: String) {
        var status = "All"
        var type = "None"
        when (taskFilterBY) {
            "All" -> {
                status = "All"
//                type = "None"
            }
            "Pending" -> {
                status = "Pending"
//                type = "None"
            }
            "Approved" -> {
                status = "Approved"
//                type = "None"
            }
            "Complete" -> {
                status = "Complete"
//                type = "None"
            }
            "Rejected" -> {
                status = "Rejected"
//                type = "Group"
            }
        }

        dataModel.GetOrderList(app.GetUserSession().id, status, type)
    }

    override fun OnScrolledToBottom() {
        view.ShowLoadMoreButton()
    }


    //============= Invoked by Data Model =============
    override fun OnGetOrderListRequestSuccess(response: List<OrderObject>) {
        view.HideEmptyView()
        view.StopSwipLoading()
        view.ClearOrderList()
        view.SetFilterText()

        if (response.isEmpty()) {
            view.ShowEmptyView()
        } else {
            response.forEach {
                view.AddOrderToRecyclerView(it)
            }
        }
    }

    override fun OnGetOrderListRequestFailed(error: Error) {
        view.StopSwipLoading()
        view.HideLoadMoreProgress()
        view.HideLoadMoreButton()
        Handler(Looper.getMainLooper()).post {
            HomeActivity.ShowErrorToast(error.Message)
            view.ShowStatusError(error.Message)
        }
    }
}