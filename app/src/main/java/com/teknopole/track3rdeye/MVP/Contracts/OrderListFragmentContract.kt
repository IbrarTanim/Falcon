package com.teknopole.track3rdeye.MVP.Contracts

import com.teknopole.track3rdeye.ObjectModels.OrderObject
import com.teknopole.track3rdeye.Utils.APIClient.Error

interface OrderListFragmentContract {
    interface Presenter {
        fun RequestToLoadOrderList(taskFilterBY: String)
        fun RequestRefreshOrderList(taskFilterBY: String)
        fun OnScrolledToBottom()

        // from data model
        fun OnGetOrderListRequestSuccess(response: List<OrderObject>)

        fun OnGetOrderListRequestFailed(error: Error)
    }

    interface View {
        fun StopSwipLoading()
        fun StartSwipeLoading()

        fun ShowLoadMoreButton()
        fun HideLoadMoreButton()

        fun ShowLoadMoreProgress()
        fun HideLoadMoreProgress()


        fun AddOrderToRecyclerView(task: OrderObject)


        fun ClearOrderList()
        fun ShowEmptyView()
        fun HideEmptyView()
        fun ShowStatusError(message: String)
        fun SetFilterText()
    }

    interface DataModel {
        fun GetOrderList(userId: Int, status: String, type: String)
    }
}