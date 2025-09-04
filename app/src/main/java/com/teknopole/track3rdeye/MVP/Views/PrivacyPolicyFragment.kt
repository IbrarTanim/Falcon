package com.teknopole.track3rdeye.MVP.Views


import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.teknopole.track3rdeye.MVP.Contracts.PrivacyPolicyContract
import com.teknopole.track3rdeye.MVP.Presenters.PrivacyPolicyPresenter
import com.teknopole.track3rdeye.R
import com.teknopole.track3rdeye.Utils.APIClient.Error
import kotlinx.android.synthetic.main.fragment_privacy_policy.*


class PrivacyPolicyFragment : Fragment(), PrivacyPolicyContract.View {

    private lateinit var presenter: PrivacyPolicyPresenter
    private lateinit var listener: ActionListener
    var requiredAccept: Boolean = true


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_privacy_policy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (!requiredAccept) {
            btnAccept.visibility = View.GONE
        }

        presenter = PrivacyPolicyPresenter(context!!, this)
        initEvent()
    }

    override fun onStart() {
        super.onStart()
        presenter.LoadPrivacyPolicyFile()
    }

    private fun initEvent() {
        btnAccept.setOnClickListener { presenter.OnPrivacyPolicyAcceptButtonClicked() }
    }


    //============ By Presenter ===========
    override fun EnableAcceptButton() {
        btnAccept.isEnabled = true
    }

    override fun DisableAcceptButton() {
        btnAccept.isEnabled = false
    }

    override fun StartLoadingProgress() {
        try {
            progressBar.visibility = View.VISIBLE
        } catch (e: Exception) {
        }
    }

    override fun StopLoadingProgress() {
        try {
            progressBar.visibility = View.GONE
        } catch (e: Exception) {
        }
    }

    override fun LoadPrivacyPolicy(url: String?) {
        try {
            if (url != null) {
                val doc = "http://docs.google.com/gview?embedded=true&url=$url"
//                val doc = "<iframe src='http://docs.google.com/gview?embedded=true&url=$url' width='100%' height='100%' style='border: none;'></iframe>"

                Log.d("laod url", doc)

                pdfViewer.settings.javaScriptEnabled = true
                pdfViewer.settings.allowFileAccess = true
                pdfViewer.loadUrl(doc)
//                pdfViewer.loadData(doc, "text/html", "UTF-8")
                pdfViewer.webViewClient = object : WebViewClient() {
                    override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                        super.onReceivedError(view, request, error)
                        Log.d("Error", error.toString())
                        presenter.OnPrivacyPolicyDownloadFailed(Error(Message = "Loading error !"))
                    }

                    override fun onPageFinished(view: WebView, url: String) {
                        Handler().postDelayed(Runnable {
                            StopLoadingProgress()
                            EnableAcceptButton()
                        }, 200)
                    }
                }

            } else {
                presenter.OnPrivacyPolicyDownloadFailed(Error(Message = "Loading error !"))
            }
        } catch (e: Exception) {
        }
    }

    override fun LoadPrivacyPolicy(bytes: ByteArray?) {
//        try {
//            if (bytes != null) {
////                pdfViewer.fromBytes(bytes)
////                        .onLoad {
////                            StopLoadingProgress()
////                            EnableAcceptButton()
////                        }
////                        .onError {
////                            DisableAcceptButton()
////                            presenter.OnPrivacyPolicyDownloadFailed(Error(Message = "Loading error !"))
////                        }
////                        .onPageError { _, _ ->  presenter.OnPrivacyPolicyDownloadFailed(Error(Message = "Page loading error !")) }
////                        .load()
//            val doc = "<iframe src='http://docs.google.com/gview?embedded=true&url=$url' width='100%' height='100%' style='border: none;'></iframe>"
//
//            pdfViewer.settings.javaScriptEnabled = true
//            pdfViewer.settings.allowFileAccess = true
//            pdfViewer.loadData(doc, "text/html", "UTF-8")
//            pdfViewer.webViewClient = object : WebViewClient() {
//
//                override fun onPageFinished(view: WebView, url: String) {
//
//                    pDialog.dismiss()
//                }
//            }
//
//            }else {
//                presenter.OnPrivacyPolicyDownloadFailed(Error(Message = "Loading error !"))
//            }
//        } catch (e: Exception) {
//        }
    }

    override fun OnPrivacyPolicyAccepteSuccess() {
        listener.OnPrivacyPolicyAccepted()
    }

    //============ Event ============
    fun SetEventListener(listener: ActionListener) {
        this.listener = listener
    }

    interface ActionListener {
        fun OnPrivacyPolicyAccepted()
    }
}
