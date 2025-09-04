package com.teknopole.track3rdeye.MVP.Presenters

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.MVP.Contracts.DealerListFragmentContract
import com.teknopole.track3rdeye.MVP.DataModels.DealerListFragmentDataModel
import com.teknopole.track3rdeye.MVP.Views.HomeActivity
import com.teknopole.track3rdeye.ObjectModels.DealerObject
import com.teknopole.track3rdeye.Utils.APIClient.Error

class DealerListFragmentPresenter(private val context: Context, private val view: DealerListFragmentContract.View) : DealerListFragmentContract.Presenter {
    val dataModel = DealerListFragmentDataModel(this)


    override fun RequestToLoadDealersList(taskFilterBY: String) {
        Thread {
            try {
                view.StartSwipeLoading()
                view.HideLoadMoreProgress()
                view.HideLoadMoreButton()

                GetDealersListWithFilter(taskFilterBY)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    override fun RequestRefreshDealersList(taskFilterBY: String) {
        Thread {
            try {
                view.HideLoadMoreProgress()
                view.HideLoadMoreButton()

                GetDealersListWithFilter(taskFilterBY)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun GetDealersListWithFilter(taskFilterBY: String) {
        var status = "All"
        var type = "None"

        when (taskFilterBY) {
            "All" -> {
                status = "All"
                type = "None"
            }
            "Assigned" -> {
                status = "Assigned"
                type = "None"
            }
            "In-Progress" -> {
                status = "In-Progress"
                type = "None"
            }
            "Complete" -> {
                status = "Complete"
                type = "None"
            }
            "Group" -> {
                status = "None"
                type = "Group"
            }
            "Individual" -> {
                status = "None"
                type = "Individual"
            }
        }

        dataModel.GetDealersList(app.GetUserSession().cpProfileId, status, type)
    }

    override fun OnScrolledToBottom() {
        view.ShowLoadMoreButton()
    }

    //============= Invoked by Data Model =============
    override fun OnGetDealersListRequestSuccess(response: List<DealerObject>) {
        view.HideEmptyView()
        view.StopSwipLoading()
        view.ClearDealersList()
//        view.SetFilterText()

        if (response.isEmpty()) {
            view.ShowEmptyView()
        } else {
            response.forEach {
                view.AddTaskToRecyclerView(it)
            }
        }
    }

    override fun OnGetDealersListRequestFailed(error: Error) {
        view.StopSwipLoading()
        view.HideLoadMoreProgress()
        view.HideLoadMoreButton()
        Handler(Looper.getMainLooper()).post {
            HomeActivity.ShowErrorToast(error.Message)
            view.ShowStatusError(error.Message)
        }
    }
}