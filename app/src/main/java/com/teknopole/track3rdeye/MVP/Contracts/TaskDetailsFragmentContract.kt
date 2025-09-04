package com.teknopole.track3rdeye.MVP.Contracts

import com.teknopole.track3rdeye.ObjectModels.TaskAttachment
import com.teknopole.track3rdeye.ObjectModels.TaskObject
import com.teknopole.track3rdeye.Utils.APIClient.Error
import com.teknopole.track3rdeye.Utils.APIClient.ResponsePacket


interface TaskDetailsFragmentContract {
    interface Presenter {
        fun RequestToLoadDataOnViewCreated(taskId: Int)

        // from data model
        fun OnGetTaskDetailsRequestSuccess(response: TaskObject)

        fun OnGetTaskDetailsRequestFailed(error: Error)
        fun RequestRefreshTaskDetails(taskId: Int)
        fun OnGroupBadgeClicked(taskId: Int)
        fun OnSeeOnMapBadgeClicked(taskId: Int)
        fun OnAttachmentBadgeClicked(taskId: Int)
        fun OnSubmitButtonClicked(isStart: Boolean)
        fun OnTaskStartOrCompleteRequestSuccess(response: ResponsePacket<String>)
        fun OnTaskStartOrCompleteRequestFailed(error: Error)
        fun OnTaskUpdated(taskId: Int)

    }

    interface View {
        fun StartSwipeLoading()
        fun StopSwipLoading()
        fun ShowEmptyView()
        fun HideEmptyView()
        fun ShowStatusError(message: String)


        fun RenderDescriptionViewToUI(descriptionView: android.view.View)
        fun SetTaskDetails(taskCode: String, taskTitle: String, taskStatus: String, taskCreatedDate: String)
        fun SetTaskTypeBadge(isTaskIndividual: Boolean)
        fun SetAttachmentBadge(attachmentCount: Int)
        fun SetDeadlineTime(startTime: String, endTime: String)
        fun SetLocationText(address: String)
        fun SetSubmitButtonText(txt: String, isStartTask: Boolean)
        fun SetSubmitButtonEnabled(enabled: Boolean)
        fun ShowTaskSegment()
        fun ShowDeadlineSegment()
        fun ShowLocationSegment()
        fun HideLocationSegment()
        fun ShowDescriptionSegment()
        fun DisableSwipeLoading()
        fun HideSubmitButton()
        fun ShowTaskOverallProgressPopup(task: TaskObject)
        fun RefressTaskListFragment()
        fun ShowViewOnMapPopup(task: TaskObject)
        fun ClearTaskDescription()
        fun HideTaskSegment()
        fun HideDeadlineSegment()
        fun HideDescriptionSegment()
        fun ShowAttachmentPopup(taskAttachments: List<TaskAttachment>)
    }

    interface DataModel {
        fun RequestToGetTaskDetails(taskId: Int)
        fun RequestToAcknoledgeTask(taskId: Int, userId: Int)
        fun StartTask(taskId: Int, userId: Int)
        fun CompleteTask(taskId: Int, userId: Int)
    }
}




