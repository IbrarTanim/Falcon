package com.teknopole.track3rdeye.MVP.Contracts

import com.teknopole.track3rdeye.ObjectModels.DealerObject
import com.teknopole.track3rdeye.Utils.APIClient.Error

interface DealerListFragmentContract {
    interface Presenter {
        fun RequestToLoadDealersList(taskFilterBY: String)
        fun RequestRefreshDealersList(taskFilterBY: String)
        fun OnScrolledToBottom()

        // from data model
        fun OnGetDealersListRequestSuccess(response: List<DealerObject>)

        fun OnGetDealersListRequestFailed(error: Error)

    }

    interface View {
        fun StopSwipLoading()
        fun StartSwipeLoading()

        fun onExpandFab()
        fun onCollapseFab()

        fun ShowLoadMoreButton()
        fun HideLoadMoreButton()

        fun ShowLoadMoreProgress()
        fun HideLoadMoreProgress()


        fun AddTaskToRecyclerView(task: DealerObject)


        fun ClearDealersList()
        fun ShowEmptyView()
        fun HideEmptyView()
        fun ShowStatusError(message: String)
        fun SetFilterText()
    }

    interface DataModel {
        fun GetDealersList(companyId: Int, status: String, type: String)
    }
}