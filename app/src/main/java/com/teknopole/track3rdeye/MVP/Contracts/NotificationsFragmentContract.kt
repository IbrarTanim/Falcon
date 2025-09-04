package com.teknopole.track3rdeye.MVP.Contracts

import com.teknopole.track3rdeye.ObjectModels.NotificationObject
import com.teknopole.track3rdeye.Utils.APIClient.Error
import com.teknopole.track3rdeye.Utils.APIClient.ResponsePacket

interface NotificationsFragmentContract {
    interface Presenter {
        fun RequestToLoadList()
        fun RequestRefreshList()
        fun RequestLoadMore()
        fun OnGetAllNotificationByEmployeeIdRequestSuccess(response: List<NotificationObject>)
        fun OnGetAllNotificationByEmployeeIdRequestFailed(error: Error)
        fun MarkNotificationAsSeen(notification: NotificationObject)
        fun OnMarkNotificationAsSeenRequestSuccess(response: ResponsePacket<Int>)
    }

    interface View {
        fun StopSwipLoading()
        fun StartSwipeLoading()


        fun ShowLoadMoreProgress()
        fun HideLoadMoreProgress()


        fun AddNotificationToRecyclerView(notification: NotificationObject)
        fun AddNotificationsToRecyclerView(notificationList: ArrayList<NotificationObject>)


        fun ClearList()
        fun ShowStatusError(message: String)
        fun ShowEmptyView()
        fun HideEmptyView()
    }

    interface DataModel {
        fun GetAllNotificationByEmployeeId(empId: Int)
        fun RequestMarkNotificationAsSeen(notificationId: Int, empId: Int)
    }
}