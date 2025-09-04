package com.teknopole.track3rdeye.Utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v4.content.LocalBroadcastManager
import com.teknopole.track3rdeye.App.app

private const val actionName = "Action"
private const val taskID = "taskId"
private const val titleName = "title"
private const val msgName = "msg"

abstract class LiveChange {
    class HomeActivity{
        companion object {
            private var listener:LiveChangeListener?=null
            private const val actionFilterName= "HomeActivity_LiveChange"


            // registerer and unregister
            fun registerLiveChangeListener(context: Context,liveChangeListener:LiveChangeListener) {
                listener= liveChangeListener
                LocalBroadcastManager.getInstance(context).registerReceiver(localBroadcastReceiver, IntentFilter(actionFilterName))
            }
            fun unregisterLiveChangeListener(context: Context) {
                LocalBroadcastManager.getInstance(context).unregisterReceiver(localBroadcastReceiver)
            }


            private enum class Events{
                OnPrivacyPolicyChanged,
                OnEmployeeHasBeenBlocked
            }

            // -========================  request methods ===================
            fun liveOnPrivacyPolicyChanged(title:String, msg: String) {
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(Intent(actionFilterName)
                        .putExtra(actionName,Events.OnPrivacyPolicyChanged)
                        .putExtra(titleName,title)
                        .putExtra(msgName,msg)
                )
            }
            fun liveOnEmployeeHasBeenBlocked(title:String, msg: String) {
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(Intent(actionFilterName)
                        .putExtra(actionName,Events.OnEmployeeHasBeenBlocked)
                        .putExtra(titleName,title)
                        .putExtra(msgName,msg)
                )
            }


            // ======================  receiver handler ========================
            private val localBroadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent) {
                    when(intent.extras[actionName] as Events)
                    {
                        LiveChange.HomeActivity.Companion.Events.OnPrivacyPolicyChanged -> {
                            val title = intent.getStringExtra(titleName)
                            val msg = intent.getStringExtra(msgName)
                            listener?.OnPrivacyPolicyChanged(title,msg)
                        }
                        LiveChange.HomeActivity.Companion.Events.OnEmployeeHasBeenBlocked -> {
                            val title = intent.getStringExtra(titleName)
                            val msg = intent.getStringExtra(msgName)
                            listener?.OnEmployeeHasBeenBlocked(title,msg)
                        }
                    }
                }
            }
        }

        // ======== listener declaration
        interface LiveChangeListener{
            fun OnPrivacyPolicyChanged(title: String, msg: String)
            fun OnEmployeeHasBeenBlocked(title: String, msg: String)
        }
    }
    class HomeFragment{
        companion object {
            private var listener:LiveChangeListener?=null
            private const val actionFilterName= "HomeFragment_LiveChange"


            // registerer and unregister
            fun registerLiveChangeListener(context: Context,liveChangeListener:LiveChangeListener) {
                listener= liveChangeListener
                LocalBroadcastManager.getInstance(context).registerReceiver(localBroadcastReceiver, IntentFilter(actionFilterName))
            }
            fun unregisterLiveChangeListener(context: Context) {
                LocalBroadcastManager.getInstance(context).unregisterReceiver(localBroadcastReceiver)
            }

            private enum class Events{
                OnNotificationCountChanged,
                OnCompanyLogoUpdated,
                OnTrackingStarted,
                OnTrackingStopped,
                OnSignnedOut
            }

            // -========================  request methods ===================
            fun liveOnNotificationCountChanged() {
                val intent = Intent(actionFilterName)
                intent.putExtra(actionName,Events.OnNotificationCountChanged)

                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(intent)
            }
            fun liveOnCompanyLogoUpdated() {
                val intent = Intent(actionFilterName)
                intent.putExtra(actionName,Events.OnCompanyLogoUpdated)

                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(intent)
            }
            fun liveOnOnTrackingStarted() {
                val intent = Intent(actionFilterName)
                intent.putExtra(actionName,Events.OnTrackingStarted)
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(intent)
            }
            fun liveOnOnTrackingStopped() {
                val intent = Intent(actionFilterName)
                intent.putExtra(actionName,Events.OnTrackingStopped)
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(intent)
            }
            fun liveOnOnSignout() {
                val intent = Intent(actionFilterName)
                intent.putExtra(actionName,Events.OnSignnedOut)
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(intent)
            }


            // ======================  receiver handler ========================
            private val localBroadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent) {
                    when(intent.extras[actionName] as Events)
                    {
                        Events.OnNotificationCountChanged -> listener?.OnNotificationCountChanged()
                        Events.OnCompanyLogoUpdated -> listener?.OnCompanyLogoUpdated()
                        Events.OnTrackingStarted -> listener?.OnTrackingStarted()
                        Events.OnTrackingStopped -> listener?.OnTrackingStopped()
                        Events.OnSignnedOut -> listener?.OnSignnedOut()
                    }
                }
            }
        }


        // ======== listener declaration
        interface LiveChangeListener{
            fun OnNotificationCountChanged()
            fun OnCompanyLogoUpdated()
            fun OnTrackingStarted()
            fun OnTrackingStopped()
            fun OnSignnedOut()
        }
    }
    class NotificationsFragment{
        companion object {
            private var listener:LiveChangeListener?=null
            private const val actionFilterName= "NotificationsFragment_LiveChange"


            // registerer and unregister
            fun registerLiveChangeListener(context: Context,liveChangeListener:LiveChangeListener) {
                listener= liveChangeListener
                LocalBroadcastManager.getInstance(context).registerReceiver(localBroadcastReceiver, IntentFilter(actionFilterName))
            }
            fun unregisterLiveChangeListener(context: Context) {
                LocalBroadcastManager.getInstance(context).unregisterReceiver(localBroadcastReceiver)
            }


            private enum class Events{
                OnNotificationCountChanged
            }

            // -========================  request methods ===================
            fun liveOnNotificationCountChanged() {
                val intent = Intent(actionFilterName)
                intent.putExtra(actionName,Events.OnNotificationCountChanged)

                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(intent)
            }


            // ======================  receiver handler ========================
            private val localBroadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent) {
                    when(intent.extras[actionName] as Events)
                    {
                        Events.OnNotificationCountChanged -> listener?.OnNotificationCountChanged()
                    }
                }
            }
        }


        // ======== listener declaration
        interface LiveChangeListener{
            fun OnNotificationCountChanged()
        }
    }
    class TaskDetailsFragment{
        companion object {
            private var listener:LiveChangeListener?=null
            private const val actionFilterName= "TaskDetailsFragment_LiveChange"


            // registerer and unregister
            fun registerLiveChangeListener(context: Context,liveChangeListener:LiveChangeListener) {
                listener= liveChangeListener
                LocalBroadcastManager.getInstance(context).registerReceiver(localBroadcastReceiver, IntentFilter(actionFilterName))
            }
            fun unregisterLiveChangeListener(context: Context) {
                LocalBroadcastManager.getInstance(context).unregisterReceiver(localBroadcastReceiver)
            }


            private enum class Events{
                OnTaskUpdated,
                OnTaskDescriptionAdded,
                OnTaskAttachmentAdded,
                OnTaskAttachmentDeleted,
                OnTaskMemberAdded,
                OnTaskMemberRemoved
            }

            // -========================  request methods ===================
            fun liveOnTaskUpdated(taskId: Int) {
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(Intent(actionFilterName)
                        .putExtra(actionName,Events.OnTaskUpdated)
                        .putExtra(taskID,taskId)
                )
            }
            fun liveOnTaskDescriptionAdded(taskId: Int) {
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(Intent(actionFilterName)
                        .putExtra(actionName,Events.OnTaskDescriptionAdded)
                        .putExtra(taskID,taskId)
                )
            }
            fun liveOnTaskAttachmentAdded(taskId: Int) {
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(Intent(actionFilterName)
                        .putExtra(actionName,Events.OnTaskAttachmentAdded)
                        .putExtra(taskID,taskId)
                )
            }
            fun liveOnTaskAttachmentDeleted(taskId: Int) {
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(Intent(actionFilterName)
                        .putExtra(actionName,Events.OnTaskAttachmentDeleted)
                        .putExtra(taskID,taskId)
                )
            }
            fun liveOnTaskMemberAdded(taskId: Int) {
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(Intent(actionFilterName)
                        .putExtra(actionName,Events.OnTaskMemberAdded)
                        .putExtra(taskID,taskId)
                )
            }
            fun liveOnTaskMemberRemoved(taskId: Int) {
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(Intent(actionFilterName)
                        .putExtra(actionName,Events.OnTaskMemberRemoved)
                        .putExtra(taskID,taskId)
                )
            }


            // ======================  receiver handler ========================
            private val localBroadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent) {
                    when(intent.extras[actionName] as Events)
                    {
                        Events.OnTaskUpdated -> listener?.OnTaskUpdated(intent.getIntExtra(taskID,-1))
                        Events.OnTaskDescriptionAdded ->listener?.OnTaskDescriptionAdded(intent.getIntExtra(taskID,-1))
                        Events.OnTaskAttachmentAdded -> listener?.OnTaskAttachmentAdded(intent.getIntExtra(taskID,-1))
                        Events.OnTaskAttachmentDeleted -> listener?.OnTaskAttachmentDeleted(intent.getIntExtra(taskID,-1))
                        Events.OnTaskMemberAdded -> listener?.OnTaskMemberAdded(intent.getIntExtra(taskID,-1))
                        Events.OnTaskMemberRemoved -> listener?.OnTaskMemberRemoved(intent.getIntExtra(taskID,-1))
                    }
                }
            }
        }


        // ======== listener declaration
        interface LiveChangeListener{
            fun OnTaskUpdated(taskId: Int)
            fun OnTaskDescriptionAdded(taskId: Int)
            fun OnTaskAttachmentAdded(taskId: Int)
            fun OnTaskAttachmentDeleted(taskId: Int)
            fun OnTaskMemberAdded(taskId: Int)
            fun OnTaskMemberRemoved(taskId: Int)
        }
    }
    class TaskListFragment{
        companion object {
            private var listener:LiveChangeListener?=null
            private const val actionFilterName= "TaskListFragment_LiveChange"


            // registerer and unregister
            fun registerLiveChangeListener(context: Context,liveChangeListener:LiveChangeListener) {
                listener= liveChangeListener
                LocalBroadcastManager.getInstance(context).registerReceiver(localBroadcastReceiver, IntentFilter(actionFilterName))
            }
            fun unregisterLiveChangeListener(context: Context) {
                LocalBroadcastManager.getInstance(context).unregisterReceiver(localBroadcastReceiver)
            }


            private enum class Events{
                OnNewTaskAdded,
                OnTaskUpdated,
                OnTaskDescriptionAdded,
                OnTaskAttachmentAdded,
                OnTaskAttachmentDeleted,
                OnTaskMemberAdded,
                OnTaskMemberRemoved
            }

            // -========================  request methods ===================
            fun liveOnNewTaskAdded() {
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(Intent(actionFilterName)
                        .putExtra(actionName,Events.OnNewTaskAdded)
                )
            }
            fun liveOnTaskUpdated(taskId: Int) {
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(Intent(actionFilterName)
                        .putExtra(actionName,Events.OnTaskUpdated)
                        .putExtra(taskID,taskId)
                )
            }
            fun liveOnTaskDescriptionAdded(taskId: Int) {
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(Intent(actionFilterName)
                        .putExtra(actionName,Events.OnTaskDescriptionAdded)
                        .putExtra(taskID,taskId)
                )
            }
            fun liveOnTaskAttachmentAdded(taskId: Int) {
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(Intent(actionFilterName)
                        .putExtra(actionName,Events.OnTaskAttachmentAdded)
                        .putExtra(taskID,taskId)
                )
            }
            fun liveOnTaskAttachmentDeleted(taskId: Int) {
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(Intent(actionFilterName)
                        .putExtra(actionName,Events.OnTaskAttachmentDeleted)
                        .putExtra(taskID,taskId)
                )
            }
            fun liveOnTaskMemberAdded(taskId: Int) {
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(Intent(actionFilterName)
                        .putExtra(actionName,Events.OnTaskMemberAdded)
                        .putExtra(taskID,taskId)
                )
            }
            fun liveOnTaskMemberRemoved(taskId: Int) {
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(Intent(actionFilterName)
                        .putExtra(actionName,Events.OnTaskMemberRemoved)
                        .putExtra(taskID,taskId)
                )
            }


            // ======================  receiver handler ========================
            private val localBroadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent) {
                    when(intent.extras[actionName] as Events)
                    {
                        Events.OnNewTaskAdded-> listener?.OnNewTaskAdded()
                        Events.OnTaskUpdated -> listener?.OnTaskUpdated(intent.getIntExtra(taskID,-1))
                        Events.OnTaskDescriptionAdded ->listener?.OnTaskDescriptionAdded(intent.getIntExtra(taskID,-1))
                        Events.OnTaskAttachmentAdded -> listener?.OnTaskAttachmentAdded(intent.getIntExtra(taskID,-1))
                        Events.OnTaskAttachmentDeleted -> listener?.OnTaskAttachmentDeleted(intent.getIntExtra(taskID,-1))
                        Events.OnTaskMemberAdded -> listener?.OnTaskMemberAdded(intent.getIntExtra(taskID,-1))
                        Events.OnTaskMemberRemoved -> listener?.OnTaskMemberRemoved(intent.getIntExtra(taskID,-1))
                    }
                }
            }
        }


        // ======== listener declaration
        interface LiveChangeListener{
            fun OnNewTaskAdded()
            fun OnTaskUpdated(taskId: Int)
            fun OnTaskDescriptionAdded(taskId: Int)
            fun OnTaskAttachmentAdded(taskId: Int)
            fun OnTaskAttachmentDeleted(taskId: Int)
            fun OnTaskMemberAdded(taskId: Int)
            fun OnTaskMemberRemoved(taskId: Int)
        }
    }

    class OrdersListFragment{
        companion object {
            private var listener:LiveChangeListener?=null
            private const val actionFilterName= "OrderListFragment_LiveChange"

            // registerer and unregister
            fun registerLiveChangeListener(context: Context,liveChangeListener:LiveChangeListener) {
                listener= liveChangeListener
                LocalBroadcastManager.getInstance(context).registerReceiver(localBroadcastReceiver, IntentFilter(actionFilterName))
            }
            fun unregisterLiveChangeListener(context: Context) {
                LocalBroadcastManager.getInstance(context).unregisterReceiver(localBroadcastReceiver)
            }


            private enum class Events{
                OnNewOrderAdded,
                OnOrderUpdated,
                OnOrderDescriptionAdded,
                OnOrderAttachmentAdded,
                OnOrderAttachmentDeleted,
                OnOrderMemberAdded,
                OnOrderMemberRemoved
            }

            // -========================  request methods ===================
            fun liveOnNewOrderAdded() {
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(Intent(actionFilterName)
                        .putExtra(actionName,Events.OnNewOrderAdded)
                )
            }
            fun liveOnOrderUpdated(taskId: Int) {
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(Intent(actionFilterName)
                        .putExtra(actionName,Events.OnOrderUpdated)
                        .putExtra(taskID,taskId)
                )
            }
            fun liveOnOrderDescriptionAdded(taskId: Int) {
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(Intent(actionFilterName)
                        .putExtra(actionName,Events.OnOrderDescriptionAdded)
                        .putExtra(taskID,taskId)
                )
            }
            fun liveOnTaskAttachmentAdded(taskId: Int) {
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(Intent(actionFilterName)
                        .putExtra(actionName,Events.OnOrderAttachmentAdded)
                        .putExtra(taskID,taskId)
                )
            }
            fun liveOnTaskAttachmentDeleted(taskId: Int) {
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(Intent(actionFilterName)
                        .putExtra(actionName,Events.OnOrderAttachmentDeleted)
                        .putExtra(taskID,taskId)
                )
            }
            fun liveOnTaskMemberAdded(taskId: Int) {
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(Intent(actionFilterName)
                        .putExtra(actionName,Events.OnOrderMemberAdded)
                        .putExtra(taskID,taskId)
                )
            }
            fun liveOnTaskMemberRemoved(taskId: Int) {
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(Intent(actionFilterName)
                        .putExtra(actionName,Events.OnOrderMemberRemoved)
                        .putExtra(taskID,taskId)
                )
            }


            // ======================  receiver handler ========================
            private val localBroadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent) {
                    when(intent.extras[actionName] as Events)
                    {
                        Events.OnNewOrderAdded-> listener?.OnNewOrderAdded()
                        Events.OnOrderUpdated -> listener?.OnOrderUpdated(intent.getIntExtra(taskID,-1))
                        Events.OnOrderDescriptionAdded ->listener?.OnOrderDescriptionAdded(intent.getIntExtra(taskID,-1))
                        Events.OnOrderAttachmentAdded -> listener?.OnOrderAttachmentAdded(intent.getIntExtra(taskID,-1))
                        Events.OnOrderAttachmentDeleted -> listener?.OnOrderAttachmentDeleted(intent.getIntExtra(taskID,-1))
                        Events.OnOrderMemberAdded -> listener?.OnOrderMemberAdded(intent.getIntExtra(taskID,-1))
                        Events.OnOrderMemberRemoved -> listener?.OnOrderMemberRemoved(intent.getIntExtra(taskID,-1))
                    }
                }
            }
        }


        // ======== listener declaration
        interface LiveChangeListener{
            fun OnNewOrderAdded()
            fun OnOrderUpdated(OrderId: Int)
            fun OnOrderDescriptionAdded(taskId: Int)
            fun OnOrderAttachmentAdded(taskId: Int)
            fun OnOrderAttachmentDeleted(taskId: Int)
            fun OnOrderMemberAdded(taskId: Int)
            fun OnOrderMemberRemoved(taskId: Int)
        }
    }

    class OrderDetailsFragment{
        companion object {
            private var listener:LiveChangeListener?=null
            private const val actionFilterName= "OrderDetailsFragment_LiveChange"


            // registerer and unregister
            fun registerLiveChangeListener(context: Context,liveChangeListener:LiveChangeListener) {
                listener= liveChangeListener
                LocalBroadcastManager.getInstance(context).registerReceiver(localBroadcastReceiver, IntentFilter(actionFilterName))
            }
            fun unregisterLiveChangeListener(context: Context) {
                LocalBroadcastManager.getInstance(context).unregisterReceiver(localBroadcastReceiver)
            }


            private enum class Events{
                OnOrderUpdated,
                OnOrderDescriptionAdded,
                OnOrderAttachmentAdded,
                OnOrderAttachmentDeleted,
                OnOrderMemberAdded,
                OnOrderMemberRemoved
            }

            // -========================  request methods ===================
            fun liveOnOrderUpdated(orderId: Int) {
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(Intent(actionFilterName)
                        .putExtra(actionName,Events.OnOrderUpdated)
                        .putExtra(taskID,orderId)
                )
            }
            fun liveOnOrderDescriptionAdded(orderId: Int) {
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(Intent(actionFilterName)
                        .putExtra(actionName,Events.OnOrderDescriptionAdded)
                        .putExtra(taskID,orderId)
                )
            }
            fun liveOnOrderAttachmentAdded(orderId: Int) {
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(Intent(actionFilterName)
                        .putExtra(actionName,Events.OnOrderAttachmentAdded)
                        .putExtra(taskID,orderId)
                )
            }
            fun liveOnOrderAttachmentDeleted(orderId: Int) {
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(Intent(actionFilterName)
                        .putExtra(actionName,Events.OnOrderAttachmentDeleted)
                        .putExtra(taskID,orderId)
                )
            }
            fun liveOnOrderMemberAdded(orderId: Int) {
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(Intent(actionFilterName)
                        .putExtra(actionName,Events.OnOrderMemberAdded)
                        .putExtra(taskID,orderId)
                )
            }
            fun liveOnOrderMemberRemoved(orderId: Int) {
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(Intent(actionFilterName)
                        .putExtra(actionName,Events.OnOrderMemberRemoved)
                        .putExtra(taskID,orderId)
                )
            }


            // ======================  receiver handler ========================
            private val localBroadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent) {
                    when(intent.extras[actionName] as Events)
                    {
                        Events.OnOrderUpdated -> listener?.OnOrderUpdated(intent.getIntExtra(taskID,-1))
                        Events.OnOrderDescriptionAdded ->listener?.OnOrderDescriptionAdded(intent.getIntExtra(taskID,-1))
                        Events.OnOrderAttachmentAdded -> listener?.OnOrderAttachmentAdded(intent.getIntExtra(taskID,-1))
                        Events.OnOrderAttachmentDeleted -> listener?.OnOrderAttachmentDeleted(intent.getIntExtra(taskID,-1))
                        Events.OnOrderMemberAdded -> listener?.OnOrderMemberAdded(intent.getIntExtra(taskID,-1))
                        Events.OnOrderMemberRemoved -> listener?.OnOrderMemberRemoved(intent.getIntExtra(taskID,-1))
                    }
                }
            }
        }


        // ======== listener declaration
        interface LiveChangeListener{
            fun OnOrderUpdated(orderId: Int)
            fun OnOrderDescriptionAdded(orderId: Int)
            fun OnOrderAttachmentAdded(orderId: Int)
            fun OnOrderAttachmentDeleted(orderId: Int)
            fun OnOrderMemberAdded(orderId: Int)
            fun OnOrderMemberRemoved(orderId: Int)
        }
    }

    class DealersListFragment{
        companion object {
            private var listener:LiveChangeListener?=null
            private const val actionFilterName= "DealerListFragment_LiveChange"

            // registerer and unregister
            fun registerLiveChangeListener(context: Context,liveChangeListener:LiveChangeListener) {
                listener= liveChangeListener
                LocalBroadcastManager.getInstance(context).registerReceiver(localBroadcastReceiver, IntentFilter(actionFilterName))
            }
            fun unregisterLiveChangeListener(context: Context) {
                LocalBroadcastManager.getInstance(context).unregisterReceiver(localBroadcastReceiver)
            }


            private enum class Events{
                OnNewDealerAdded,
                OnDealerUpdated,
                OnDealerDescriptionAdded,
                OnDealerAttachmentAdded,
                OnDealerAttachmentDeleted,
                OnDealerMemberAdded,
                OnDealerMemberRemoved
            }

            // -========================  request methods ===================
            fun liveOnNewDealerAdded() {
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(Intent(actionFilterName)
                        .putExtra(actionName,Events.OnNewDealerAdded)
                )
            }
            fun liveOnDealerUpdated(taskId: Int) {
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(Intent(actionFilterName)
                        .putExtra(actionName,Events.OnDealerUpdated)
                        .putExtra(taskID,taskId)
                )
            }
            fun liveOnDealerDescriptionAdded(taskId: Int) {
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(Intent(actionFilterName)
                        .putExtra(actionName,Events.OnDealerDescriptionAdded)
                        .putExtra(taskID,taskId)
                )
            }
            fun liveOnTaskAttachmentAdded(taskId: Int) {
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(Intent(actionFilterName)
                        .putExtra(actionName,Events.OnDealerAttachmentAdded)
                        .putExtra(taskID,taskId)
                )
            }
            fun liveOnTaskAttachmentDeleted(taskId: Int) {
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(Intent(actionFilterName)
                        .putExtra(actionName,Events.OnDealerAttachmentDeleted)
                        .putExtra(taskID,taskId)
                )
            }
            fun liveOnTaskMemberAdded(taskId: Int) {
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(Intent(actionFilterName)
                        .putExtra(actionName,Events.OnDealerMemberAdded)
                        .putExtra(taskID,taskId)
                )
            }
            fun liveOnTaskMemberRemoved(taskId: Int) {
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(Intent(actionFilterName)
                        .putExtra(actionName,Events.OnDealerMemberRemoved)
                        .putExtra(taskID,taskId)
                )
            }


            // ======================  receiver handler ========================
            private val localBroadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent) {
                    when(intent.extras[actionName] as Events)
                    {
                        Events.OnNewDealerAdded-> listener?.OnNewDealerAdded()
                        Events.OnDealerUpdated -> listener?.OnDealerUpdated(intent.getIntExtra(taskID,-1))
                        Events.OnDealerDescriptionAdded ->listener?.OnDealerDescriptionAdded(intent.getIntExtra(taskID,-1))
                        Events.OnDealerAttachmentAdded -> listener?.OnDealerAttachmentAdded(intent.getIntExtra(taskID,-1))
                        Events.OnDealerAttachmentDeleted -> listener?.OnDealerAttachmentDeleted(intent.getIntExtra(taskID,-1))
                        Events.OnDealerMemberAdded -> listener?.OnDealerMemberAdded(intent.getIntExtra(taskID,-1))
                        Events.OnDealerMemberRemoved -> listener?.OnDealerMemberRemoved(intent.getIntExtra(taskID,-1))
                    }
                }
            }
        }


        // ======== listener declaration
        interface LiveChangeListener{
            fun OnNewDealerAdded()
            fun OnDealerUpdated(taskId: Int)
            fun OnDealerDescriptionAdded(taskId: Int)
            fun OnDealerAttachmentAdded(taskId: Int)
            fun OnDealerAttachmentDeleted(taskId: Int)
            fun OnDealerMemberAdded(taskId: Int)
            fun OnDealerMemberRemoved(taskId: Int)
        }
    }

    class ProductDetailsFragment{
        companion object {
            private var listener:LiveChangeListener?=null
            private const val actionFilterName= "DealerDetailsFragment_LiveChange"


            // registerer and unregister
            fun registerLiveChangeListener(context: Context,liveChangeListener:LiveChangeListener) {
                listener= liveChangeListener
                LocalBroadcastManager.getInstance(context).registerReceiver(localBroadcastReceiver, IntentFilter(actionFilterName))
            }
            fun unregisterLiveChangeListener(context: Context) {
                LocalBroadcastManager.getInstance(context).unregisterReceiver(localBroadcastReceiver)
            }


            private enum class Events{
                OnProductUpdated,
                OnProductDescriptionAdded,
                OnProductAttachmentAdded,
                OnProductAttachmentDeleted,
                OnProductMemberAdded,
                OnProductMemberRemoved
            }

            // -========================  request methods ===================
            fun liveOnProductUpdated(taskId: Int) {
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(Intent(actionFilterName)
                        .putExtra(actionName,Events.OnProductUpdated)
                        .putExtra(taskID,taskId)
                )
            }
            fun liveOnProductDescriptionAdded(taskId: Int) {
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(Intent(actionFilterName)
                        .putExtra(actionName,Events.OnProductDescriptionAdded)
                        .putExtra(taskID,taskId)
                )
            }
            fun liveOnProductAttachmentAdded(taskId: Int) {
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(Intent(actionFilterName)
                        .putExtra(actionName,Events.OnProductAttachmentAdded)
                        .putExtra(taskID,taskId)
                )
            }
            fun liveOnProductAttachmentDeleted(taskId: Int) {
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(Intent(actionFilterName)
                        .putExtra(actionName,Events.OnProductAttachmentDeleted)
                        .putExtra(taskID,taskId)
                )
            }
            fun liveOnProductMemberAdded(taskId: Int) {
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(Intent(actionFilterName)
                        .putExtra(actionName,Events.OnProductMemberAdded)
                        .putExtra(taskID,taskId)
                )
            }
            fun liveOnProductMemberRemoved(taskId: Int) {
                LocalBroadcastManager.getInstance(app.appContext).sendBroadcast(Intent(actionFilterName)
                        .putExtra(actionName,Events.OnProductMemberRemoved)
                        .putExtra(taskID,taskId)
                )
            }


            // ======================  receiver handler ========================
            private val localBroadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent) {
//                    when(intent.extras[actionName] as Events)
//                    {
//                        Events.OnProductUpdated -> listener?.OnProductUpdated(intent.getIntExtra(taskID,-1))
//                        Events.OnProductDescriptionAdded ->listener?.OnProductDescriptionAdded(intent.getIntExtra(taskID,-1))
//                        Events.OnProductAttachmentAdded -> listener?.OnProductAttachmentAdded(intent.getIntExtra(taskID,-1))
//                        Events.OnProductAttachmentDeleted -> listener?.OnProductAttachmentDeleted(intent.getIntExtra(taskID,-1))
//                        Events.OnProductMemberAdded -> listener?.OnProductMemberAdded(intent.getIntExtra(taskID,-1))
//                        Events.OnProductMemberRemoved -> listener?.OnProductMemberRemoved(intent.getIntExtra(taskID,-1))
//                    }
                }
            }
        }


        // ======== listener declaration
        interface LiveChangeListener{
//            fun OnProductUpdated(taskId: Int)
//            fun OnProductDescriptionAdded(taskId: Int)
//            fun OnProductAttachmentAdded(taskId: Int)
//            fun OnProductAttachmentDeleted(taskId: Int)
//            fun OnProductMemberAdded(taskId: Int)
//            fun OnProductMemberRemoved(taskId: Int)
        }
    }
}