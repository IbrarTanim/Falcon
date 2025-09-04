package com.teknopole.track3rdeye.PopupAndDialogs

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.ObjectModels.Employee
import com.teknopole.track3rdeye.MVP.Views.HomeActivity
import com.teknopole.track3rdeye.R
import com.teknopole.track3rdeye.Utils.APIClient.Error
import com.teknopole.track3rdeye.Utils.APIClient.RESTClient
import com.teknopole.track3rdeye.Utils.APIClient.ResponsePacket
import com.teknopole.track3rdeye.Utils.APIClient.ServerResponse
import com.teknopole.track3rdeye.Utils.DeviceInfo
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by MD. ABDUR ROUF on 5/24/2018.
 */
class ChangePasswordDialog(private val context: Context) {

    private var alertDialogBuilder: AlertDialog = AlertDialog.Builder(context).create()
    private val btnCancel:TextView
    private val btnUpdate:TextView
    init {
        val v =LayoutInflater.from(context).inflate(R.layout.change_password_dialog, null)
        alertDialogBuilder.setView(v)
        alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.setCanceledOnTouchOutside(false)
      //  alertDialogBuilder.window!!.setLayout(200, 200)
        alertDialogBuilder.show()


        alertDialogBuilder.window.setDimAmount(0.05f)
        alertDialogBuilder.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialogBuilder.window!!.setLayout((DeviceInfo.displayWidth(context) * .95).toInt(), alertDialogBuilder.window!!.attributes.height)

        btnCancel=  v.findViewById(R.id.btnCancel)
        btnUpdate=  v.findViewById(R.id.btnUpdate)


        btnCancel.setOnClickListener { Close() }
        btnUpdate.setOnClickListener {
            HomeActivity.HideKeyboard(v.findFocus())
            val etOldPass = v.findViewById<EditText>(R.id.etUsername)
            val etNewPass = v.findViewById<EditText>(R.id.etNewPassword)
            val etConfirmPass = v.findViewById<EditText>(R.id.etConfirmPassword)

           when{
               etOldPass.text.isEmpty() -> HomeActivity.ShowWarningToast("Existing password is required !")
               etNewPass.text.isEmpty() -> HomeActivity.ShowWarningToast("New password is required !")
               etNewPass.text.length <4 || etNewPass.text.length >10 -> HomeActivity.ShowWarningToast("Password can be minimum 4 and maximum 10 characters !")
               etConfirmPass.text.isEmpty() -> HomeActivity.ShowWarningToast("Confirm password is required !")
               etOldPass.text.toString() == etNewPass.text.toString() ->  HomeActivity.ShowWarningToast("Your existing password and new password are same. Can not proceed !")
               etNewPass.text.toString() != etConfirmPass.text.toString() ->  HomeActivity.ShowWarningToast("Your new password and confirm password did not match !")
               else-> UpdatePassword(etOldPass.text.toString(), etNewPass.text.toString())
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


    private fun UpdatePassword(oldPass: String, newPass: String) {
        try {
            btnCancel.isEnabled = false
            btnUpdate.isEnabled = false
            HomeActivity.StartLoading()
            RESTClient.GetClient(UrlClient::class.java).ChangePassword(app.GetUserSession().id,oldPass,newPass)
                    .enqueue(object : ServerResponse<ResponsePacket<Employee>>(app.appContext){
                        override fun OnComplete(response: ResponsePacket<Employee>) {
                            btnCancel.isEnabled = true
                            btnUpdate.isEnabled = true

                            when{
                                response.IsReport == "Ok" ->{
                                    HomeActivity.ShowSuccessToast(response.Message)
                                    Close()
                                }
                                response.IsReport == "ExistingAndNewPasswordSame" ->{
                                    HomeActivity.ShowWarningToast(response.Message)
                                }
                                response.IsReport == "IncorrectExistingPassword" ->{
                                    HomeActivity.ShowErrorToast(response.Message)
                                }
                                response.IsReport == "IncorrectExistingPassword" ->{
                                    HomeActivity.ShowErrorToast(response.Message)
                                }
                            }
                        }
                        override fun OnError(error: Error) {
                            btnCancel.isEnabled = true
                            btnUpdate.isEnabled = true
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
        @POST("Api/Mobile/ChangePassword/{empId}/{extPass}/{newPass}")
        fun ChangePassword(@Path("empId") employeeId:Int, @Path("extPass") existingPass:String, @Path("newPass") newPass:String): Call<ResponsePacket<Employee>>
    }
}