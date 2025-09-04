package com.teknopole.track3rdeye.MVP.Presenters

import android.content.Context
import android.os.Handler
import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.MVP.Contracts.PrivacyPolicyContract
import com.teknopole.track3rdeye.MVP.DataModels.PrivacyPolicyDataModel
import com.teknopole.track3rdeye.MVP.Views.HomeActivity
import com.teknopole.track3rdeye.ObjectModels.LoginResponse
import com.teknopole.track3rdeye.Utils.APIClient.Error
import com.teknopole.track3rdeye.Utils.APIClient.RESTClient
import com.teknopole.track3rdeye.Utils.APIClient.ResponsePacket
import okhttp3.ResponseBody


class PrivacyPolicyPresenter(private val context: Context, private val view: PrivacyPolicyContract.View) : PrivacyPolicyContract.Presenter {
    private val dataModel = PrivacyPolicyDataModel(this)


    // ================== from view ===================
    override fun LoadPrivacyPolicyFile() {
        view.StartLoadingProgress()
        view.DisableAcceptButton()


        view.LoadPrivacyPolicy(RESTClient.baseUrl + "Company_PrivacyPolicy/${app.GetUserSession().privacyPolicy}")
//        dataModel.RequestDownloadPrivacyPolicyFile(app.GetUserSession().privacyPolicy)
    }

    override fun OnPrivacyPolicyAcceptButtonClicked() {
        view.DisableAcceptButton()
        HomeActivity.StartLoading()
        dataModel.RequestAcceptPrivacyPolicy(app.GetUserSession().id)
    }


    //================ From data model ===================
    override fun OnPrivacyPolicyDownloadSuccess(response: ResponseBody) {
        view.LoadPrivacyPolicy(response.bytes())
    }

    override fun OnPrivacyPolicyDownloadFailed(error: Error) {
        HomeActivity.ShowErrorToast(error.Message)
        Handler().postDelayed({
            LoadPrivacyPolicyFile()
        }, 4500)
    }

    override fun OnAcceptPrivacyPolicyRequestSuccess(response: ResponsePacket<LoginResponse>) {
        when {
            response.IsReport == "Ok" -> {
                HomeActivity.CompleteLoading(true)
                app.SetPirvacyPolicyAccepted(true)
                view.OnPrivacyPolicyAccepteSuccess()
            }
            else -> {
                HomeActivity.ShowErrorToast(response.Message)
                view.EnableAcceptButton()
            }
        }
        view.EnableAcceptButton()
    }

    override fun OnAcceptPrivacyPolicyRequestFailed(error: Error) {
        HomeActivity.ShowErrorToast(error.Message)
        view.EnableAcceptButton()
    }
}