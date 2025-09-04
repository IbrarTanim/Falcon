package com.teknopole.track3rdeye.MVP.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.teknopole.track3rdeye.ObjectModels.TaskObject
import com.teknopole.track3rdeye.R
import com.teknopole.track3rdeye.Utils.Convert


/**
 * Created by Md. Abdur Rouf -03 on 5/15/2018.
 */
class TaskListRecyclerAdapter(private var taskList: List<TaskObject>, private val listener: EventListener) : RecyclerView.Adapter<TaskListRecyclerAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTaskId = view.findViewById<TextView>(R.id.tvTaskId)!!
        val tvTaskTitle = view.findViewById<TextView>(R.id.tvTaskTitle)!!
        val tvCreatedTime = view.findViewById<TextView>(R.id.tvCreatedTime)!!
        val tvTaskType = view.findViewById<TextView>(R.id.tvTaskType)!!
        val tvTaskStatus = view.findViewById<TextView>(R.id.tvTaskStatus)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.template_task_list, parent, false)
        val vh = ViewHolder(v)

        v.setOnClickListener {
            listener.OnItemClicked(taskList[vh.adapterPosition])
        }
        return vh
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvTaskId.text = taskList[position].taskCode
        holder.tvTaskTitle.text = taskList[position].taskTitle
        holder.tvTaskType.text = taskList[position].taskType
        holder.tvTaskStatus.text = taskList[position].taskStatus
        holder.tvCreatedTime.text = Convert.FormatDateTime(taskList[position].taskCreatedOn, "dd MMM, yyyy")
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerViewScrollListener() {
            override fun onScrolledToBottom() {
                listener.OnScrolledToBottom()
            }
        })
    }

    interface EventListener {
        fun OnItemClicked(task: TaskObject)
        fun OnScrolledToBottom()

    }
}