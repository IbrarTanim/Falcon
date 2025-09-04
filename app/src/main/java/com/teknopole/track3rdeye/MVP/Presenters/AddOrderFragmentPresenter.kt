package com.teknopole.track3rdeye.MVP.Presenters

import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.MVP.Contracts.AddOrderFragmentContract
import com.teknopole.track3rdeye.MVP.DataModels.AddOrderFragmentDataModel
import com.teknopole.track3rdeye.MVP.Views.HomeActivity
import com.teknopole.track3rdeye.ObjectModels.*
import com.teknopole.track3rdeye.Utils.APIClient.Error
import com.teknopole.track3rdeye.Utils.APIClient.ResponsePacket

class AddOrderFragmentPresenter(private val view: AddOrderFragmentContract.View) : AddOrderFragmentContract.Presenter {


    val dataModel: AddOrderFragmentContract.Model = AddOrderFragmentDataModel(this)
    lateinit var dealerList: List<DealerObject>
    lateinit var productList: List<ProductObject>

    lateinit var selectedDealer: DealerObject
   val addedProductList = arrayListOf<OrderProductDetail>()

    override fun RequestToLoadDealerList() {
        dataModel.RequestToGetDealerList(app.GetUserSession().cpProfileId)
    }

    override fun RequestToLoadProductList() {
        dataModel.RequestToGetProductList(app.GetUserSession().cpProfileId)
    }

    override fun OnUpdateEmployeeResuestSuccess(responsePacket: ResponsePacket<Employee>) {
        when {
            responsePacket.IsReport == "Ok" -> {
                view.CompleteLoadingToast(true)
                app.SetUserSession(responsePacket.Content)
                view.OnAddOrderSuccess()
                return
            }
            responsePacket.IsReport == "NotOk" -> {
                view.ShowErrorToast(responsePacket.Message)
            }
            else -> view.ShowWarningToast(responsePacket.Message)
        }
        view.CompleteLoadingToast(false)
        view.ChangeSubmitButtonEnabled(true)
    }

    override fun OnUpdateEmployeeRequestFailed(error: Error) {
        view.ChangeSubmitButtonEnabled(true)
        view.CompleteLoadingToast(false)
        view.ShowErrorToast(error.Message)
    }


    //============ Invoked by Data Model =============
    override fun OnGetDealersListRequestSuccess(response: List<DealerObject>) {
        this.dealerList = response

        view.SetDealerListToView(dealerList)
    }

    override fun OnGetDealersListRequestFailed(error: Error) {
        HomeActivity.ShowErrorToast(error.Message)
    }

    override fun OnGetProductListRequestSuccess(response: List<ProductObject>) {
        this.productList = response
        view.SetProductListToView(productList)
    }

    override fun OnGetProductListRequestFailed(error: Error) {
        HomeActivity.ShowErrorToast(error.Message)
    }


    override fun OnProductAdded(product: ProductObject) {
        if (!addedProductList.any { t-> t.productId == product.id }){
            addedProductList.add(OrderProductDetail(product))
            view.AddProductInfoToView(product)
        }
    }

    private fun OrderProductDetail(product: ProductObject): OrderProductDetail {
       return OrderProductDetail(product.id,product.productName, product.productCode, product.unitPrice, product.salesPrice, 0.0,0.0, product.currentStockQty)
    }

    override fun OnProductRemoved(position: Int) {
        addedProductList.removeAt(position)
        view.RemoveProductInfoFromView(position)
    }


    override fun onDealerSelected(selectedDealer: DealerObject) {
        this.selectedDealer = selectedDealer
    }

    override fun OnSubmitButtonClicked(order: OrderObject, updateOrder: Boolean) {
        when{
//            order.firstName.isEmpty() -> view.ShowWarningToast("First name is required!")
//            order.lastName.isEmpty() -> view.ShowWarningToast("Last name is required!")
//            order.username.isEmpty() -> view.ShowWarningToast("Username is required!")
//            order.username.length <6 || order.username.length >=12 -> HomeActivity.ShowWarningToast("Username can be minimum 6 and maximum 11 characters !")
//            order.email.isEmpty() -> view.ShowWarningToast("Email address is required!")
//            order.mobile.isEmpty() -> view.ShowWarningToast("Mobile number is required!")
//            order.birthDate.isEmpty() -> view.ShowWarningToast("Birth date is required!")
//            order.gender.isEmpty() -> view.ShowWarningToast("Gender is required!")
//            order.address.isEmpty() -> view.ShowWarningToast("Address is required!")
//            !Validator.isEmailValid(order.email) -> view.ShowWarningToast("Ops! Invalid email address")
//            !Validator.isMobileNumberValid(order.mobile) ->view.ShowWarningToast("Ops! Invalid mobile number")

            else ->
            {
                view.ChangeSubmitButtonEnabled(false)
                view.ShowLoadingToast()

                val orderpostobject = OrderPostObject(order, order.productDetails)
                if (updateOrder) {
                    dataModel.RequestUpdateOrder(orderpostobject)
                } else {
                    dataModel.RequestAddNewOrder(orderpostobject)
                }
            }
        }
    }

    override fun OnAddOrderSuccess() {
        view.CompleteLoadingToast(true)
        view.ChangeSubmitButtonEnabled(true)
    }

    override fun OnAddOrderFailed(error: Error) {
        view.ChangeSubmitButtonEnabled(true)
        view.ShowErrorToast(error.Message)
    }

    override fun OnUpdateOrderFailed(error: Error) {
        view.ChangeSubmitButtonEnabled(true)
        view.ShowErrorToast(error.Message)
    }

    override fun OnUpdateOrderSuccess() {
        view.CompleteLoadingToast(true)
        view.ChangeSubmitButtonEnabled(true)
    }

    override fun RequestToLoadOrderDetails(orderId: Int) {
        HomeActivity.StartLoading()
        Thread {
            dataModel.RequestToGetOrderDetails(orderId)
        }.start()
    }

    override fun OnGetOrderDetailsRequestSuccess(response: OrderObject) {

        HomeActivity.CompleteLoading(true)
        view.SetOrderDetailsToView(response)
    }

    override fun OnGetOrderDetailsRequestFailed(error: Error) {
        HomeActivity.ShowErrorToast(error.Message)
    }

}