package com.teknopole.track3rdeye.MVP.Presenters

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.widget.TextView
import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.MVP.Contracts.ProductDetailsFragmentContract
import com.teknopole.track3rdeye.MVP.DataModels.ProductDetailsFragmentDataModel
import com.teknopole.track3rdeye.MVP.Views.HomeActivity
import com.teknopole.track3rdeye.ObjectModels.ProductObject
import com.teknopole.track3rdeye.R
import com.teknopole.track3rdeye.Utils.APIClient.Error
import com.teknopole.track3rdeye.Utils.APIClient.ResponsePacket
import com.teknopole.track3rdeye.Utils.Convert
import java.util.*

class ProductDetailsFragmentPresenter(private val context: Context, private val view: ProductDetailsFragmentContract.View) : ProductDetailsFragmentContract.Presenter {
    val dataModel = ProductDetailsFragmentDataModel(this)
    private lateinit var product: ProductObject

    private fun GenerateProductSegment(product: ProductObject) {
        view.ShowProductSegment(product)
    }


    //============== Invoked by view ===============
    override fun RequestToLoadProductDetails(productId: Int) {
        view.StartSwipeLoading()
        Thread {
            dataModel.RequestToGetProductDetails(productId)
        }.start()
    }

    override fun RequestRefreshProductDetails(productId: Int) {
        Thread {
            dataModel.RequestToGetProductDetails(productId)
        }.start()
    }

    //============== live change request ==============


    //============ Invoked by Data Model =============
    override fun OnGetProductDetailsRequestSuccess(product: ProductObject) {
        this.product = product

        view.StopSwipLoading()
        view.HideEmptyView()

        GenerateProductSegment(product)

    }

    override fun OnGetProductDetailsRequestFailed(error: Error) {
        view.StopSwipLoading()
        Handler(Looper.getMainLooper()).post {
            HomeActivity.ShowErrorToast(error.Message)
            view.ShowStatusError(error.Message)
        }
    }


}