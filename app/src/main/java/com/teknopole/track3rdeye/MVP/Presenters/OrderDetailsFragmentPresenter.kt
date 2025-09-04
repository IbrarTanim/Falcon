package com.teknopole.track3rdeye.MVP.Presenters

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.teknopole.track3rdeye.MVP.Contracts.OrderDetailsFragmentContract
import com.teknopole.track3rdeye.MVP.DataModels.OrderDetailsFragmentDataModel
import com.teknopole.track3rdeye.MVP.Views.HomeActivity
import com.teknopole.track3rdeye.ObjectModels.OrderObject
import com.teknopole.track3rdeye.Utils.APIClient.Error

class OrderDetailsFragmentPresenter(private val context: Context, private val view: OrderDetailsFragmentContract.View) : OrderDetailsFragmentContract.Presenter {
    val dataModel = OrderDetailsFragmentDataModel(this)
    private lateinit var order: OrderObject




    //============== Invoked by view ===============
    override fun RequestToLoadOrderDetails(taskId: Int) {
        view.StartSwipeLoading()
        Thread {
            dataModel.RequestToGetOrderDetails(taskId)
        }.start()
    }

    override fun RequestRefreshOrderDetails(taskId: Int) {
        Thread {
            dataModel.RequestToGetOrderDetails(taskId)
        }.start()
    }



    //============== live change request ==============
    override fun OnOrderUpdated(orderId: Int) {
        Thread {
            try {
                dataModel.RequestToGetOrderDetails(orderId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }


    //============ Invoked by Data Model =============
    override fun OnGetOrderDetailsRequestSuccess(order: OrderObject) {
        this.order = order

        view.StopSwipLoading()
        view.HideEmptyView()

       view.SetOrderDetailsToView(order)
    }

    override fun OnGetOrderDetailsRequestFailed(error: Error) {
        view.StopSwipLoading()
        Handler(Looper.getMainLooper()).post {
            HomeActivity.ShowErrorToast(error.Message)
            view.ShowStatusError(error.Message)
        }
    }


}