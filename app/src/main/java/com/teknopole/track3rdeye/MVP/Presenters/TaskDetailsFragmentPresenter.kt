package com.teknopole.track3rdeye.MVP.Presenters

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.widget.TextView
import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.MVP.Contracts.TaskDetailsFragmentContract
import com.teknopole.track3rdeye.MVP.DataModels.TaskDetailsFragmentDataModel
import com.teknopole.track3rdeye.MVP.Views.HomeActivity
import com.teknopole.track3rdeye.ObjectModels.TaskObject
import com.teknopole.track3rdeye.R
import com.teknopole.track3rdeye.Utils.APIClient.Error
import com.teknopole.track3rdeye.Utils.APIClient.ResponsePacket
import com.teknopole.track3rdeye.Utils.Convert
import java.util.*

class TaskDetailsFragmentPresenter(private val context: Context, private val view: TaskDetailsFragmentContract.View) : TaskDetailsFragmentContract.Presenter {
    val dataModel = TaskDetailsFragmentDataModel(this)
    private lateinit var task: TaskObject

    private fun GenerateTaskSegment(task: TaskObject) {
        try {
            val myStatusOnThisTask = (task.taskMembers.find { member -> member.employeeId == app.GetUserSession().id })
            val taskStatus = myStatusOnThisTask?.groupMemberTaskStatus!!


            view.SetTaskDetails(task.taskCode, task.taskTitle, taskStatus, Convert.FormatDateTime(task.taskCreatedOn, "dd MMM, yyyy hh:mm a"))
            view.SetTaskTypeBadge(task.taskType == "Individual")
            view.SetAttachmentBadge(task.taskAttachments.size)


            // set button enabled or not
            val startDate = Convert.StringDateToCalender(task.taskStartTime, "yyyy-MM-dd'T'HH:mm:ss")
            if (Calendar.getInstance(Locale.getDefault()) >= startDate) {
                view.SetSubmitButtonEnabled(true)
            } else {
                view.SetSubmitButtonEnabled(false)
            }


            // set button text and if task complete then hide button
            when (taskStatus) {
                "Assigned" -> view.SetSubmitButtonText("Start this task", true)
                "In-Progress" -> view.SetSubmitButtonText("Mark as complete", false)
                else -> view.HideSubmitButton()
            }

            // hide button
            if (myStatusOnThisTask.groupMemberStatus != 1
                    || myStatusOnThisTask.groupMemberRemovalStatus.equals("Yes", true)
                    || myStatusOnThisTask.groupMemberTaskSecondaryStatus.equals("Complete", true)) {
                view.HideSubmitButton()
            }



            view.ShowTaskSegment()
            view.DisableSwipeLoading()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun GenerateDeadlineSegment(task: TaskObject) {
        try {
            val startTime = Convert.FormatDateTime(task.taskStartTime, "dd MMM, yyyy hh:mm a")
            val endTime = Convert.FormatDateTime(task.taskEndTime, "dd MMM, yyyy hh:mm a")
            view.SetDeadlineTime(startTime, endTime)


            view.ShowDeadlineSegment()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun GenerateLocationSegment(task: TaskObject) {
        if (task.taskLat > 0 && task.taskLng > 0) {
            view.ShowLocationSegment()
            Thread {
                view.SetLocationText(Convert.GetGeoCodeAddress(task.taskLat, task.taskLng))
            }.start()
        } else {
            view.HideLocationSegment()
        }
    }

    private fun GenerateTaskDescriptionAndRender(task: TaskObject) {
        try {
            view.ClearTaskDescription()
            task.taskDescriptions.forEachIndexed { index, taskDescription ->
                val description = if (index == 0) {
                    LayoutInflater.from(app.appContext).inflate(R.layout.template_task_details_chat_latest, null, false)
                } else {
                    LayoutInflater.from(context).inflate(R.layout.template_task_details_chat_old, null, false)
                }

                description.findViewById<TextView>(R.id.tvMessage).text = taskDescription.description
                description.findViewById<TextView>(R.id.tvChatTime).text = Convert.FormatDateTime(taskDescription.descriptionAddedOn, "dd MMM, yyyy hh:mm a")


                // add to ui
                view.RenderDescriptionViewToUI(description)
            }

            view.ShowDescriptionSegment()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    //============== Invoked by view ===============
    override fun RequestToLoadDataOnViewCreated(taskId: Int) {
        view.StartSwipeLoading()
        Thread {
            dataModel.RequestToGetTaskDetails(taskId)
        }.start()
    }

    override fun RequestRefreshTaskDetails(taskId: Int) {
        Thread {
            dataModel.RequestToGetTaskDetails(taskId)
        }.start()
    }

    override fun OnGroupBadgeClicked(taskId: Int) {
        view.ShowTaskOverallProgressPopup(task)
    }

    override fun OnSeeOnMapBadgeClicked(taskId: Int) {
        view.ShowViewOnMapPopup(task)
    }

    override fun OnAttachmentBadgeClicked(taskId: Int) {
        try {
            if (task.taskAttachments.isNotEmpty()) {
                view.ShowAttachmentPopup(task.taskAttachments)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun OnSubmitButtonClicked(isStart: Boolean) {
        HomeActivity.StartLoading()
        Thread {
            if (isStart) {
                dataModel.StartTask(task.id, app.GetUserSession().id)
            } else {
                dataModel.CompleteTask(task.id, app.GetUserSession().id)
            }
        }.start()
    }


    //============== live change request ==============
    override fun OnTaskUpdated(taskId: Int) {
        Thread {
            try {
                dataModel.RequestToGetTaskDetails(taskId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }


    //============ Invoked by Data Model =============
    override fun OnGetTaskDetailsRequestSuccess(task: TaskObject) {
        this.task = task

        view.StopSwipLoading()
        view.HideEmptyView()

        GenerateTaskSegment(task)
        GenerateDeadlineSegment(task)
        GenerateLocationSegment(task)
        GenerateTaskDescriptionAndRender(task)

        dataModel.RequestToAcknoledgeTask(task.id, app.GetUserSession().id)
        view.RefressTaskListFragment()
    }

    override fun OnGetTaskDetailsRequestFailed(error: Error) {
        view.StopSwipLoading()
        Handler(Looper.getMainLooper()).post {
            HomeActivity.ShowErrorToast(error.Message)
            view.ShowStatusError(error.Message)
        }
    }


    override fun OnTaskStartOrCompleteRequestSuccess(response: ResponsePacket<String>) {
        Handler(Looper.getMainLooper()).post {
            HomeActivity.CompleteLoading(true)
        }
        RequestRefreshTaskDetails(task.id)
        view.RefressTaskListFragment()
    }

    override fun OnTaskStartOrCompleteRequestFailed(error: Error) {
        Handler(Looper.getMainLooper()).post {
            HomeActivity.ShowErrorToast(error.Message)
        }
    }
}