package com.teknopole.track3rdeye.MVP.Views


import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.teknopole.track3rdeye.MVP.Contracts.TaskDetailsFragmentContract
import com.teknopole.track3rdeye.MVP.Presenters.TaskDetailsFragmentPresenter
import com.teknopole.track3rdeye.ObjectModels.TaskAttachment
import com.teknopole.track3rdeye.ObjectModels.TaskObject
import com.teknopole.track3rdeye.PopupAndDialogs.AttachmentPopup
import com.teknopole.track3rdeye.PopupAndDialogs.ConfirmationDialog
import com.teknopole.track3rdeye.PopupAndDialogs.TaskOverallProgressPopup
import com.teknopole.track3rdeye.PopupAndDialogs.ViewLocationOnMapPopup
import com.teknopole.track3rdeye.R
import com.teknopole.track3rdeye.Utils.CircularAnimationUtils
import com.teknopole.track3rdeye.Utils.LiveChange
import kotlinx.android.synthetic.main.fragment_task_details.*
import kotlinx.android.synthetic.main.operation_status_view.*


class TaskDetailsFragment : Fragment(), TaskDetailsFragmentContract.View, LiveChange.TaskDetailsFragment.LiveChangeListener {
    private lateinit var presenter: TaskDetailsFragmentPresenter
    private var instanceState: Bundle? = null
    private var attachmentPopup: AttachmentPopup? = null
    private var confirmDialog: ConfirmationDialog? = null
    private var taskOverallProgressPopup: TaskOverallProgressPopup? = null
    private var viewLocationOnMapPopup: ViewLocationOnMapPopup? = null
    var TaskId: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        instanceState = savedInstanceState
        val view = inflater.inflate(R.layout.fragment_task_details, container, false)

        view.addOnLayoutChangeListener(onLayoutChangeListener)
        TaskId = arguments!!.getInt("taskId")
        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        instanceState = savedInstanceState
        presenter = TaskDetailsFragmentPresenter(context!!, this)
        initMember()
        initEvent()

        // load task details on created
        presenter.RequestToLoadDataOnViewCreated(this.TaskId)
    }

    fun GetTaskDetails() {
        HideTaskSegment()
        HideDeadlineSegment()
        HideLocationSegment()
        HideDescriptionSegment()
        HideSubmitButton()
        StartSwipeLoading()
        attachmentPopup?.Close()

        Handler().postDelayed({
            try {
                presenter.RequestToLoadDataOnViewCreated(this.TaskId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, 100)
    }

    override fun onPause() {
        super.onPause()
        attachmentPopup?.Close()
        confirmDialog?.Close()
        taskOverallProgressPopup?.Close()
        viewLocationOnMapPopup?.Close()
    }

    override fun onStart() {
        super.onStart()
        LiveChange.TaskDetailsFragment.registerLiveChangeListener(context!!, this)
    }

    override fun onStop() {
        super.onStop()
        LiveChange.TaskDetailsFragment.unregisterLiveChangeListener(context!!)
    }

    override fun onDestroy() {
        RefressTaskListFragment()
        super.onDestroy()
    }

    private val onLayoutChangeListener = View.OnLayoutChangeListener { v, _, _, _, _, _, _, _, _ ->
        RemoveLayoutChangeListener()
        CircularAnimationUtils().RegisterCircularRevealEnterAnimation(v
                , HomeActivity.touchPosition.x
                , HomeActivity.touchPosition.y
                , v.width, v.height
                , Color.parseColor("#ff7472")
                , Color.WHITE)
    }

    private fun RemoveLayoutChangeListener() {
        view!!.removeOnLayoutChangeListener(onLayoutChangeListener)
    }


    private fun initMember() {
        swipeLayout.setColorSchemeResources(R.color.colorLightPink)
    }

    private fun initEvent() {
        btnBack.setOnClickListener { activity?.onBackPressed() }
        btnSubmit.setOnClickListener { SubmitTask() }
        swipeLayout.setOnRefreshListener { presenter.RequestRefreshTaskDetails(TaskId) }
        tvGroup.setOnClickListener { presenter.OnGroupBadgeClicked(TaskId) }
        tvSeeOnMap.setOnClickListener { presenter.OnSeeOnMapBadgeClicked(TaskId) }
        tvAttachment.setOnClickListener { presenter.OnAttachmentBadgeClicked(TaskId) }
    }

    private fun SubmitTask() {
        confirmDialog = ConfirmationDialog(context!!, object : ConfirmationDialog.ClickListener {
            override fun OnYesButtonClick() {
                presenter.OnSubmitButtonClicked(btnSubmit.tag as Boolean)
            }

            override fun OnNoButtonClick() {

            }
        }).apply {
            if (btnSubmit.tag as Boolean) {
                SetConfirmationText("Do you really want to start this task. If you start task, You will not be able to undo.")
            } else {
                SetConfirmationText("Do you really want to complete this task. If you complete, You will not be able to undo.")
            }
            BackgroundDim(false)
        }
        confirmDialog?.Show()

    }


    //============ Invoked by presenter =============
    override fun StartSwipeLoading() {
        Handler(Looper.getMainLooper()).post {
            try {
                swipeLayout.isRefreshing = true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun StopSwipLoading() {
        Handler(Looper.getMainLooper()).post {
            try {
                swipeLayout.isRefreshing = false
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    override fun ShowEmptyView() {
        Handler(Looper.getMainLooper()).post {
            try {
                OperationStatusView.visibility = View.VISIBLE
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun ShowStatusError(message: String) {
        Handler(Looper.getMainLooper()).post {
            try {
                OperationStatusView.visibility = View.VISIBLE
                tvOperationStatusText.text = message
                ivOperationStatusIcon.setImageResource(R.drawable.ic_status_error)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun HideEmptyView() {
        Handler(Looper.getMainLooper()).post {
            try {
                OperationStatusView.visibility = View.GONE
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    // task segment
    override fun SetTaskDetails(taskCode: String, taskTitle: String, taskStatus: String, taskCreatedDate: String) {
        Handler(Looper.getMainLooper()).post {
            try {
                tvTaskId.text = taskCode
                tvTaskTitle.text = taskTitle
                tvCreatedTime.text = taskCreatedDate
                tvTaskStatus.text = taskStatus
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun SetTaskTypeBadge(isTaskIndividual: Boolean) {
        Handler(Looper.getMainLooper()).post {
            try {
                if (isTaskIndividual) {
                    tvIndividual.visibility = View.VISIBLE
                    tvGroup.visibility = View.GONE
                } else {
                    tvIndividual.visibility = View.GONE
                    tvGroup.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun SetAttachmentBadge(attachmentCount: Int) {
        Handler(Looper.getMainLooper()).post {
            try {
                if (attachmentCount > 0) {

                    val text = if (attachmentCount == 1) "$attachmentCount Attachment" else "$attachmentCount Attachments"

                    tvAttachment.visibility = View.VISIBLE
                    tvAttachment.text = text
                } else {
                    tvAttachment.visibility = View.GONE
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun SetSubmitButtonText(txt: String, isStartTask: Boolean) {
        Handler(Looper.getMainLooper()).post {
            try {
                btnSubmit.text = txt
                btnSubmit.tag = isStartTask
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun ShowTaskOverallProgressPopup(task: TaskObject) {
        taskOverallProgressPopup = TaskOverallProgressPopup(task)
        taskOverallProgressPopup?.Show(view!!)
    }


    override fun ShowAttachmentPopup(taskAttachments: List<TaskAttachment>) {
        try {
            attachmentPopup = AttachmentPopup(taskAttachments)
            attachmentPopup?.Show(view!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // deadline segment
    override fun SetDeadlineTime(startTime: String, endTime: String) {
        Handler(Looper.getMainLooper()).post {
            try {
                tvStartTime.text = startTime
                tvEndTime.text = endTime
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun SetSubmitButtonEnabled(enabled: Boolean) {
        Handler(Looper.getMainLooper()).post {
            try {
                btnSubmit.visibility = View.VISIBLE
                btnSubmit.isEnabled = enabled
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun HideSubmitButton() {
        Handler(Looper.getMainLooper()).post {
            try {
                btnSubmit.visibility = View.GONE
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    // Location segment
    override fun SetLocationText(address: String) {
        Handler(Looper.getMainLooper()).post {
            try {
                tvLocationText.text = address
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun ShowViewOnMapPopup(task: TaskObject) {
        viewLocationOnMapPopup = ViewLocationOnMapPopup(task, instanceState)
        viewLocationOnMapPopup?.Show(view!!)
    }


    // description
    override fun ClearTaskDescription() {
        Handler(Looper.getMainLooper()).post {
            try {
                descriptionContainerView.removeAllViews()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun RenderDescriptionViewToUI(descriptionView: View) {
        Handler(Looper.getMainLooper()).post {
            try {
                descriptionContainerView.addView(descriptionView)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    // show or hide segment
    override fun DisableSwipeLoading() {
        Handler(Looper.getMainLooper()).post {
            try {
                swipeLayout.isEnabled = false
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun ShowTaskSegment() {
        Handler(Looper.getMainLooper()).post {
            try {
                taskBox.visibility = View.VISIBLE
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun ShowDeadlineSegment() {
        Handler(Looper.getMainLooper()).post {
            try {
                deadlineBox.visibility = View.VISIBLE
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun ShowLocationSegment() {
        Handler(Looper.getMainLooper()).post {
            try {
                locationBox.visibility = View.VISIBLE
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun HideTaskSegment() {
        Handler(Looper.getMainLooper()).post {
            try {
                taskBox.visibility = View.GONE
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun HideDeadlineSegment() {
        Handler(Looper.getMainLooper()).post {
            try {
                deadlineBox.visibility = View.GONE
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun HideDescriptionSegment() {
        Handler(Looper.getMainLooper()).post {
            try {
                detailsBox.visibility = View.GONE
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun HideLocationSegment() {
        Handler(Looper.getMainLooper()).post {
            try {
                locationBox.visibility = View.GONE
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun ShowDescriptionSegment() {
        Handler(Looper.getMainLooper()).post {
            try {
                detailsBox.visibility = View.VISIBLE
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    // after start or complete a task refresh task list fragment to load new data
    override fun RefressTaskListFragment() {
        listener.OnTaskStartOrCompleteRefreshTaskListFragment()
    }


    //=========== Live update ===========
    override fun OnTaskUpdated(taskId: Int) {
        if (this.TaskId == taskId) {
            presenter.OnTaskUpdated(taskId)
        }
    }

    override fun OnTaskDescriptionAdded(taskId: Int) {
        if (this.TaskId == taskId) {
            presenter.OnTaskUpdated(taskId)
        }
    }

    override fun OnTaskAttachmentAdded(taskId: Int) {
        if (this.TaskId == taskId) {
            presenter.OnTaskUpdated(taskId)
        }
    }

    override fun OnTaskAttachmentDeleted(taskId: Int) {
        if (this.TaskId == taskId) {
            presenter.OnTaskUpdated(taskId)
        }
    }

    override fun OnTaskMemberAdded(taskId: Int) {
        if (this.TaskId == taskId) {
            presenter.OnTaskUpdated(taskId)
        }
    }

    override fun OnTaskMemberRemoved(taskId: Int) {
        if (this.TaskId == taskId) {
            presenter.OnTaskUpdated(taskId)
        }
    }


    //============ Event ============
    private lateinit var listener: ActionListener

    fun SetEventListener(listener: ActionListener) {
        this.listener = listener
    }

    interface ActionListener {
        fun OnTaskStartOrCompleteRefreshTaskListFragment()
    }
}