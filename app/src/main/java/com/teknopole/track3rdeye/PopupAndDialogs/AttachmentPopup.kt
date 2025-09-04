package com.teknopole.track3rdeye.PopupAndDialogs

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.*
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.MVP.Views.HomeActivity
import com.teknopole.track3rdeye.ObjectModels.TaskAttachment
import com.teknopole.track3rdeye.R
import com.teknopole.track3rdeye.Utils.APIClient.RESTClient
import com.teknopole.track3rdeye.Utils.Convert


class AttachmentPopup(private val attachments: List<TaskAttachment>) {


    private var window: PopupWindow = PopupWindow(app.appContext)
    private val dimView:View
    private var dialogView: View
    private var container: LinearLayout
    private val inflater = LayoutInflater.from(app.appContext)


    init {
        val v = LayoutInflater.from(app.appContext).inflate(R.layout.popup_attachment, null)
        dimView = v.findViewById(R.id.dimView)
        dialogView = v.findViewById(R.id.dialogView)
        container = v.findViewById(R.id.container)

        window.contentView = v
        window.width = RelativeLayout.LayoutParams.MATCH_PARENT//DeviceInfo.displayWidth(app.appContext)
        window.height = RelativeLayout.LayoutParams.MATCH_PARENT
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.isOutsideTouchable = false
        window.isFocusable = false

        v.findViewById<TextView>(R.id.tvHeaderText).text = if(attachments.size >1)"Attachments" else "Attachment"
        v.findViewById<TextView>(R.id.btnGotIt).setOnClickListener {Close()}

        addAttachmentToContainer()
    }
    fun Close() {
        if (window.isShowing) {
            try {
                dimView.animate().alpha(0.0f)
                YoYo.with(Techniques.ZoomOut)
                        .onEnd {
                            try {
                                HomeActivity.SuspressBackPress=false
                                window.dismiss()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        .duration(300)
                        .playOn(dialogView)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun Show(view: View) {
        if (!window.isShowing) {
            window.showAtLocation(view, Gravity.CENTER, 0, 0)
            startAnimation()
        }
    }
    private fun startAnimation() {
        Handler().postDelayed({
            try {
                HomeActivity.SuspressBackPress=true
                dimView.animate().alpha(0.8f)
                dialogView.visibility=View.VISIBLE
                YoYo.with(Techniques.ZoomIn)
                        .duration(500)
                        .interpolate(OvershootInterpolator(.9f))
                        .playOn(dialogView)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        },15)
    }


    private fun addAttachmentToContainer() {
        try {
            container.removeAllViews()

            attachments.forEach {
                try {
                    val template = inflater.inflate(R.layout.template_attachment_item,null,false)
                    template.findViewById<TextView>(R.id.tvFileType).text = it.Attachment.substring(it.Attachment.lastIndexOf('.')+1)
                    template.findViewById<TextView>(R.id.tvFileName).text = it.FileRealName
                    template.findViewById<TextView>(R.id.tvTime).text = Convert.FormatDateTime(it.AddedOn,"dd MMM, yyyy hh:mm a")
                    template.findViewById<ImageView>(R.id.iv_bgn_download).apply {
                        tag = it.Attachment
                        setOnClickListener {
                            downloadAttachment(it.tag as String)
                        }
                    }

                    container.addView(template)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }catch (e:Exception)
        {
            e.printStackTrace()
        }
    }

    private fun downloadAttachment(fileName:String)
    {
        try {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.data = Uri.parse(RESTClient.GetTaskAttachmentUrl(fileName))
            app.appContext.startActivity(intent)
            Close()
        } catch (e: ActivityNotFoundException) {
            Close()
            HomeActivity.ShowWarningDialog("Warning..","No Web browser or downloader found! Please install any web browser or downloader first.")
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }
}