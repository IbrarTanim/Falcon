package com.teknopole.track3rdeye.PopupAndDialogs

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.MVP.Views.HomeActivity
import com.teknopole.track3rdeye.ObjectModels.Employee
import com.teknopole.track3rdeye.R
import com.teknopole.track3rdeye.Utils.APIClient.Error
import com.teknopole.track3rdeye.Utils.APIClient.RESTClient
import com.teknopole.track3rdeye.Utils.APIClient.ResponsePacket
import com.teknopole.track3rdeye.Utils.APIClient.ServerResponse
import com.teknopole.track3rdeye.Utils.DeviceInfo
import com.teknopole.track3rdeye.Utils.Validator
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by MD. ABDUR ROUF on 5/24/2018.
 */
class ForgetPasswordDialog(private val context: Context) {

    private var alertDialogBuilder: AlertDialog = AlertDialog.Builder(context).create()
    private val btnCancel:TextView
    private val btnSend:TextView
    init {
        val v =LayoutInflater.from(context).inflate(R.layout.forgetpassword_dialog_layout, null)
        alertDialogBuilder.setView(v)
        alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.setCanceledOnTouchOutside(false)
      //  alertDialogBuilder.window!!.setLayout(200,200)
        alertDialogBuilder.show()


        alertDialogBuilder.window.setDimAmount(0.05f)
        alertDialogBuilder.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialogBuilder.window!!.setLayout((DeviceInfo.displayWidth(context) * .95).toInt(), alertDialogBuilder.window!!.attributes.height)

        btnCancel=  v.findViewById(R.id.btnCancel)
        btnSend=  v.findViewById(R.id.btnSend)


        btnCancel.setOnClickListener { Close() }
        btnSend.setOnClickListener {
            try {
                HomeActivity.HideKeyboard(v.findFocus())
                val etUsername = v.findViewById<EditText>(R.id.etUsername)
                val etEmail = v.findViewById<EditText>(R.id.etEmail)
                when{
                    etUsername.text.isEmpty() && etEmail.text.isEmpty() -> HomeActivity.ShowWarningToast("Please enter your username or email address to recover your credentials.")
                    !etEmail.text.isEmpty() && !Validator.isEmailValid(etEmail.text.toString()) ->HomeActivity.ShowWarningToast("Please enter valid email address.")
                    else-> ForgotPasswordRequest(etUsername.text.toString(), etEmail.text.toString())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun Show() {
        if (!alertDialogBuilder.isShowing)
            alertDialogBuilder.show()
    }
    fun Close() {
        if(alertDialogBuilder.isShowing)
            alertDialogBuilder.dismiss()
    }

    private fun ForgotPasswordRequest(username: String, email: String) {
        try {
            val Username =if (username.isEmpty()) "u" else username
            val Email = if (email.isEmpty()) "e" else email


            btnCancel.isEnabled = false
            btnSend.isEnabled = false
            HomeActivity.StartLoading()
            RESTClient.GetClient(UrlClient::class.java).ForgotPassword(Username,Email)
                    .enqueue(object : ServerResponse<ResponsePacket<Employee>>(app.appContext){
                        override fun OnComplete(response: ResponsePacket<Employee>) {
                            btnCancel.isEnabled = true
                            btnSend.isEnabled = true

                            when{
                                response.IsReport == "Ok" ->{
                                    HomeActivity.ShowSuccessToast(response.Message)
                                    Close()
                                }
                                response.IsReport == "NotOk" ->{
                                    HomeActivity.ShowErrorToast(response.Message)
                                }
                                response.IsReport == "NoMatch" ->{
                                    HomeActivity.ShowErrorToast(response.Message)
                                }
                                response.IsReport == "UserHasNoEmail" ->{
                                    HomeActivity.ShowWarningToast(response.Message)
                                }
                            }
                        }
                        override fun OnError(error: Error) {
                            btnCancel.isEnabled = true
                            btnSend.isEnabled = true
                            HomeActivity.ShowErrorToast(error.Message)
                        }
                    })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //=========================== Server Request ==========================
    //    ******************** URL & Method **********************
    interface UrlClient {
        // agent check access
        @GET("Api/Mobile/ForgotPasswordOrUsername/{username}/{email}")
        fun ForgotPassword(@Path("username") username:String, @Path("email") email:String): Call<ResponsePacket<Employee>>
    }
}