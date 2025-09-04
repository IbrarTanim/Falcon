package com.teknopole.track3rdeye.MVP.Presenters

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.MVP.Contracts.NotificationsFragmentContract
import com.teknopole.track3rdeye.MVP.DataModels.NotificationsFragmentDataModel
import com.teknopole.track3rdeye.MVP.Views.HomeActivity
import com.teknopole.track3rdeye.ObjectModels.NotificationObject
import com.teknopole.track3rdeye.Utils.APIClient.Error
import com.teknopole.track3rdeye.Utils.APIClient.ResponsePacket

class NotificationsFragmentPresenter(private val context: Context, private val view: NotificationsFragmentContract.View) : NotificationsFragmentContract.Presenter {
    val dataModel = NotificationsFragmentDataModel(this)

    override fun RequestToLoadList() {
        view.StartSwipeLoading()
        Thread {
            dataModel.GetAllNotificationByEmployeeId(app.GetUserSession().id)
        }.start()
    }

    override fun RequestRefreshList() {
        Thread {
            dataModel.GetAllNotificationByEmployeeId(app.GetUserSession().id)
        }.start()
    }

    override fun RequestLoadMore() {
        Thread {
            dataModel.GetAllNotificationByEmployeeId(app.GetUserSession().id)
        }.start()
    }

    override fun MarkNotificationAsSeen(notification: NotificationObject) {
        if (notification.status == "Unseen") {
            Thread {
                dataModel.RequestMarkNotificationAsSeen(notification.notificationId, app.GetUserSession().id)
            }.start()
        }
    }

    // ========== By Data Model =========
    override fun OnGetAllNotificationByEmployeeIdRequestSuccess(response: List<NotificationObject>) {
        // view.HideLoadMoreProgress()
        view.StopSwipLoading()
        view.ClearList()
        if (response.isEmpty()) {
            view.ShowEmptyView()
        } else {
            view.HideEmptyView()
            response.forEach {
                view.AddNotificationToRecyclerView(it)
            }
        }
    }

    override fun OnGetAllNotificationByEmployeeIdRequestFailed(error: Error) {
        view.StopSwipLoading()
        view.HideLoadMoreProgress()
//        view.HideLoadMoreButton()
        Handler(Looper.getMainLooper()).post {
            HomeActivity.ShowErrorToast(error.Message)
            view.ShowStatusError(error.Message)
        }
    }

    override fun OnMarkNotificationAsSeenRequestSuccess(response: ResponsePacket<Int>) {
        app.UpdateUnseenNotificationCount(response.Content)
        RequestRefreshList()
    }
}