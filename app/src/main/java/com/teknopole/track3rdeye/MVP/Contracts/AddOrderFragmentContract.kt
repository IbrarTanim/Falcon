package com.teknopole.track3rdeye.MVP.Contracts

import com.teknopole.track3rdeye.ObjectModels.*
import com.teknopole.track3rdeye.Utils.APIClient.Error
import com.teknopole.track3rdeye.Utils.APIClient.ResponsePacket

interface AddOrderFragmentContract {
    interface View {


        fun ShowWarningToast(msg: String)
        fun ShowLoadingToast()
        fun CompleteLoadingToast(isSuccess: Boolean)
        fun ShowErrorToast(message: String)

        fun OnAddOrderSuccess()


        fun ChangeSubmitButtonEnabled(enable: Boolean)
        fun SetDealerListToView(dealerList: List<DealerObject>)
        fun SetProductListToView(productList: List<ProductObject>)
        fun AddProductInfoToView(product: ProductObject)
        fun RemoveProductInfoFromView(position: Int)

        fun SetOrderDetailsToView(order: OrderObject)
    }

    interface Presenter {
        fun OnUpdateEmployeeResuestSuccess(responsePacket: ResponsePacket<Employee>)
        fun OnUpdateEmployeeRequestFailed(error: Error)


        fun RequestToLoadDealerList()
        fun RequestToLoadProductList()
        fun OnSubmitButtonClicked(order: OrderObject, updateOrder: Boolean)

        fun OnGetDealersListRequestSuccess(response: List<DealerObject>)
        fun OnGetDealersListRequestFailed(error: Error)

        fun OnGetProductListRequestSuccess(response: List<ProductObject>)
        fun OnGetProductListRequestFailed(error: Error)

        fun OnProductAdded(product: ProductObject)
        fun onDealerSelected(selectedDealer: DealerObject)
        fun OnProductRemoved(position: Int)
        fun OnAddOrderSuccess()
        fun OnAddOrderFailed(error: Error)
        fun OnUpdateOrderFailed(error: Error)
        fun OnUpdateOrderSuccess()
        fun RequestToLoadOrderDetails(orderId: Int)
        fun OnGetOrderDetailsRequestSuccess(response: OrderObject)
        fun OnGetOrderDetailsRequestFailed(error: Error)
    }

    interface Model {
        fun RequestAddNewOrder(order: OrderPostObject)
        fun RequestToGetDealerList(cpProfileId: Int)
        fun RequestToGetProductList(cpProfileId: Int)
        fun RequestUpdateOrder(order: OrderPostObject)
        fun RequestToGetOrderDetails(orderId: Int)
    }
}