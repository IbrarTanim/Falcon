package com.teknopole.track3rdeye.MVP.Contracts

import com.teknopole.track3rdeye.ObjectModels.OrderObject
import com.teknopole.track3rdeye.ObjectModels.ProductObject
import com.teknopole.track3rdeye.ObjectModels.TaskAttachment
import com.teknopole.track3rdeye.Utils.APIClient.Error
import com.teknopole.track3rdeye.Utils.APIClient.ResponsePacket

interface ProductDetailsFragmentContract {
    interface Presenter {
        fun RequestToLoadProductDetails(productId: Int)
        fun RequestRefreshProductDetails(productId: Int)

        // from data model
        fun OnGetProductDetailsRequestSuccess(response: ProductObject)
        fun OnGetProductDetailsRequestFailed(error: Error)
    }

    interface View {
        fun StartSwipeLoading()
        fun StopSwipLoading()
        fun ShowEmptyView()
        fun HideEmptyView()
        fun ShowStatusError(message: String)


        fun ShowProductSegment(productId: ProductObject)
    }

    interface DataModel {
        fun RequestToGetProductDetails(productId: Int)
    }
}