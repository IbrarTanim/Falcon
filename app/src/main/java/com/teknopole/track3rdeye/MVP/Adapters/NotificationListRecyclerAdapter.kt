package com.teknopole.track3rdeye.MVP.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.teknopole.track3rdeye.ObjectModels.NotificationObject
import com.teknopole.track3rdeye.R
import com.teknopole.track3rdeye.Utils.Convert


/**
 * Created by Md. Abdur Rouf -03 on 5/15/2018.
 */
class NotificationListRecyclerAdapter(private var notificationList: List<NotificationObject>, private val listener: ItemClickListener) : RecyclerView.Adapter<NotificationListRecyclerAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)!!
        val tvTime = view.findViewById<TextView>(R.id.tvTime)!!
        val tvMessage = view.findViewById<TextView>(R.id.tvMessage)!!
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (notificationList[position].status == "Seen") 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = if (viewType == 1) {
            LayoutInflater.from(parent.context).inflate(R.layout.template_notification_list_us, parent, false)
        } else {
            LayoutInflater.from(parent.context).inflate(R.layout.template_notification_list, parent, false)
        }

        val vh = ViewHolder(v)
        v.setOnClickListener {
            listener.OnItemClicked(notificationList[vh.adapterPosition])
        }
        return vh
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvTitle.text = notificationList[position].title
        holder.tvTime.text = Convert.FormatDateTime(notificationList[position].generatedOn, "dd/MM/YY")
        holder.tvMessage.text = notificationList[position].message
    }

    interface ItemClickListener {
        fun OnItemClicked(notification: NotificationObject)
    }
}