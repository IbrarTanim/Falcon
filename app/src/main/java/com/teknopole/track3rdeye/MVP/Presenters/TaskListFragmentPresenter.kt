package com.teknopole.track3rdeye.MVP.Presenters

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.MVP.Contracts.TaskListFragmentContract
import com.teknopole.track3rdeye.MVP.DataModels.TaskListFragmentDataModel
import com.teknopole.track3rdeye.MVP.Views.HomeActivity
import com.teknopole.track3rdeye.ObjectModels.TaskObject
import com.teknopole.track3rdeye.Utils.APIClient.Error

class TaskListFragmentPresenter(private val context: Context, private val view: TaskListFragmentContract.View) : TaskListFragmentContract.Presenter {
    val dataModel = TaskListFragmentDataModel(this)


    override fun RequestToLoadTaskList(taskFilterBY: String) {
        Thread {
            try {
                view.StartSwipeLoading()
                view.HideLoadMoreProgress()
                view.HideLoadMoreButton()

                GetTaskListWithFilter(taskFilterBY)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    override fun RequestRefreshTaskList(taskFilterBY: String) {
        Thread {
            try {
                view.HideLoadMoreProgress()
                view.HideLoadMoreButton()

                GetTaskListWithFilter(taskFilterBY)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun GetTaskListWithFilter(taskFilterBY: String) {
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

        dataModel.GetTaskList(app.GetUserSession().id, status, type)
    }

    override fun OnScrolledToBottom() {
        view.ShowLoadMoreButton()
    }


    //============= Invoked by Data Model =============
    override fun OnGetTaskListRequestSuccess(response: List<TaskObject>) {
        view.HideEmptyView()
        view.StopSwipLoading()
        view.ClearTaskList()
        view.SetFilterText()

        if (response.isEmpty()) {
            view.ShowEmptyView()
        } else {
            response.forEach {
                view.AddTaskToRecyclerView(it)
            }
        }
    }

    override fun OnGetTaskListRequestFailed(error: Error) {
        view.StopSwipLoading()
        view.HideLoadMoreProgress()
        view.HideLoadMoreButton()
        Handler(Looper.getMainLooper()).post {
            HomeActivity.ShowErrorToast(error.Message)
            view.ShowStatusError(error.Message)
        }
    }
}