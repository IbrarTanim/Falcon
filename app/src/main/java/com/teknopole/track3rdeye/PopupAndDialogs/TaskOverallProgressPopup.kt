package com.teknopole.track3rdeye.PopupAndDialogs

import android.animation.Animator
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.*
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.assist.ImageSize
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener
import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.MVP.Views.HomeActivity
import com.teknopole.track3rdeye.ObjectModels.TaskObject
import com.teknopole.track3rdeye.R
import com.teknopole.track3rdeye.Utils.APIClient.RESTClient

class TaskOverallProgressPopup(private val task: TaskObject) {
    private var window: PopupWindow = PopupWindow(app.appContext)
    private val dimView:View
    private var dialogView: View

    init {
        val v = LayoutInflater.from(app.appContext).inflate(R.layout.popup_task_overall_progress, null)
        dimView = v.findViewById(R.id.dimView)
        dialogView = v.findViewById(R.id.dialogView)

        window.contentView = v
        window.width = RelativeLayout.LayoutParams.MATCH_PARENT//DeviceInfo.displayWidth(app.appContext)
        window.height = RelativeLayout.LayoutParams.MATCH_PARENT
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.isOutsideTouchable = false
        window.isFocusable = false
        v.findViewById<TextView>(R.id.btnGotIt).setOnClickListener {Close()}

        SetOverallDetails(dialogView)
        SetUserStatuses(dialogView)

        ImageLoader.getInstance().init(app.GetUILConfig())
    }
    fun Close() {
        if (window.isShowing) {
            try {
                dimView.animate().alpha(0.0f)
                YoYo.with(Techniques.ZoomOut)
                        .withListener(object :Animator.AnimatorListener{
                            override fun onAnimationRepeat(animation: Animator?) {

                            }
                            override fun onAnimationCancel(animation: Animator?) {

                            }
                            override fun onAnimationStart(animation: Animator?) {

                            }

                            override fun onAnimationEnd(animation: Animator?) {
                                try {
                                    HomeActivity.SuspressBackPress=false
                                    window.dismiss()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        })
                        .duration(300)
                        .playOn(dialogView)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun Show(view: View) {
        if (!window.isShowing)
            window.showAtLocation(view,Gravity.CENTER,0,0)

        startAnimation()
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
        },10)
    }


    private fun SetOverallDetails(v: View) {
        try {
            v.findViewById<TextView>(R.id.tvGroupName).text = task.groupName
            v.findViewById<TextView>(R.id.tvOverallStatus).text = task.overallTaskStatus
            v.findViewById<TextView>(R.id.tvTotalMembersCount).text = task.noOfGroupMembers.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun SetUserStatuses(v: View) {
        val container = v.findViewById<LinearLayout>(R.id.userListBox)
        task.taskMembers.forEachIndexed { index, member ->
            val u = LayoutInflater.from(app.appContext).inflate(R.layout.template_task_overall_progress_user_status,null,false)
            u.findViewById<TextView>(R.id.tvFullName).text = member.groupMemberFirstName +" "+member.groupMemberLastName
            u.findViewById<TextView>(R.id.tvDesignation).text = member.groupMemberDesignation
            u.findViewById<TextView>(R.id.tvStatus).text = member.groupMemberTaskStatus

            if (!member.groupMemberTaskSecondaryStatus.isNullOrEmpty()) {
                u.findViewById<TextView>(R.id.tvSecondaryStatus).apply {
                    text = "Marked as ${member.groupMemberTaskSecondaryStatus} by admin"
                    visibility = View.VISIBLE
                }
            }else {
                u.findViewById<TextView>(R.id.tvSecondaryStatus).visibility = View.GONE
            }

            val tvBlockAndRemoveStatus = u.findViewById<TextView>(R.id.tvActiveRemoveStatus)
            when
            {
                member.groupMemberStatus == 0 && !member.groupMemberRemovalStatus.isNullOrEmpty()->{
                    tvBlockAndRemoveStatus.text= "Blocked & Removed"
                    tvBlockAndRemoveStatus.visibility = View.VISIBLE
                }
                member.groupMemberStatus == 1 && !member.groupMemberRemovalStatus.isNullOrEmpty()->{
                    tvBlockAndRemoveStatus.text= "Removed"
                    tvBlockAndRemoveStatus.visibility = View.VISIBLE
                }
                member.groupMemberStatus == 0 && member.groupMemberRemovalStatus.isNullOrEmpty()->{
                    tvBlockAndRemoveStatus.text= "Blocked"
                    tvBlockAndRemoveStatus.visibility = View.VISIBLE
                }
                else -> {
                    tvBlockAndRemoveStatus.visibility = View.GONE
                }
            }

            if(index != 0) u.findViewById<View>(R.id.topSeparator).visibility = View.GONE
            container.addView(u)


            val url = if (!member.groupMemberPhoto.isNullOrEmpty())
               RESTClient.GetEmployeeImageUrl(member.groupMemberPhoto)
            else RESTClient.GetDefaultEmployeeImageUrl(member.groupMemberGender == "Male")
            LoadImage(url,u.findViewById(R.id.ivProfilePic))
        }
    }

    private fun LoadImage(imageUrl:String,imageView: ImageView)
    {
        try {
            ImageLoader.getInstance()
                    .apply {
                        clearMemoryCache()
                        clearDiskCache()
                        displayImage(imageUrl,imageView, ImageSize(50,50))
                    }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}