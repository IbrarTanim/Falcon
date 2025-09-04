package com.teknopole.track3rdeye.MVP.Contracts

import com.teknopole.track3rdeye.ObjectModels.TaskObject
import com.teknopole.track3rdeye.Utils.APIClient.Error

interface TaskListFragmentContract {
    interface Presenter {
        fun RequestToLoadTaskList(taskFilterBY: String)
        fun RequestRefreshTaskList(taskFilterBY: String)
        fun OnScrolledToBottom()

        // from data model
        fun OnGetTaskListRequestSuccess(response: List<TaskObject>)

        fun OnGetTaskListRequestFailed(error: Error)
    }

    interface View {
        fun StopSwipLoading()
        fun StartSwipeLoading()

        fun ShowLoadMoreButton()
        fun HideLoadMoreButton()

        fun ShowLoadMoreProgress()
        fun HideLoadMoreProgress()


        fun AddTaskToRecyclerView(task: TaskObject)


        fun ClearTaskList()
        fun ShowEmptyView()
        fun HideEmptyView()
        fun ShowStatusError(message: String)
        fun SetFilterText()
    }

    interface DataModel {
        fun GetTaskList(userId: Int, status: String, type: String)
    }
}