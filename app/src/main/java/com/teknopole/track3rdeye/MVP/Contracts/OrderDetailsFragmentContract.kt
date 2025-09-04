package com.teknopole.track3rdeye.MVP.Contracts

import com.teknopole.track3rdeye.ObjectModels.OrderObject
import com.teknopole.track3rdeye.ObjectModels.TaskAttachment
import com.teknopole.track3rdeye.Utils.APIClient.Error
import com.teknopole.track3rdeye.Utils.APIClient.ResponsePacket

interface OrderDetailsFragmentContract {
    interface Presenter {
        fun RequestToLoadOrderDetails(orderId: Int)
        fun RequestRefreshOrderDetails(orderId: Int)

        // from data model
        fun OnGetOrderDetailsRequestSuccess(response: OrderObject)
        fun OnGetOrderDetailsRequestFailed(error: Error)
        fun OnOrderUpdated(orderId: Int)
    }

    interface View {
        fun StartSwipeLoading()
        fun StopSwipLoading()
        fun ShowEmptyView()
        fun HideEmptyView()
        fun ShowStatusError(message: String)


        fun SetOrderDetailsToView(order: OrderObject)


        fun DisableSwipeLoading()
    }

    interface DataModel {
        fun RequestToGetOrderDetails(orderId: Int)
    }
}