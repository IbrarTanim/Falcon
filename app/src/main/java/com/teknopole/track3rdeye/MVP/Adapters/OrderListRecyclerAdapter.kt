package com.teknopole.track3rdeye.MVP.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.teknopole.track3rdeye.ObjectModels.OrderObject
import com.teknopole.track3rdeye.R
import com.teknopole.track3rdeye.Utils.Convert


/**
 * Created by Md. Abdur Rouf -03 on 5/15/2018.
 */
class OrderListRecyclerAdapter(private var orderList: List<OrderObject>, private val listener: EventListener) : RecyclerView.Adapter<OrderListRecyclerAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvOrderId = view.findViewById<TextView>(R.id.tvOrderId)!!
        val tvDealerTitle = view.findViewById<TextView>(R.id.tvDealerTitle)!!
        val tvCreatedTime = view.findViewById<TextView>(R.id.tvCreatedTime)!!
        val tvOrderAmount = view.findViewById<TextView>(R.id.tvOrderAmount)!!
        val tvOrderStatus = view.findViewById<TextView>(R.id.tvOrderStatus)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.template_order_list, parent, false)
        val vh = ViewHolder(v)

        v.setOnClickListener {
            listener.OnItemClicked(orderList[vh.adapterPosition])
        }
        return vh
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvOrderId.text = orderList[position].orderCode
        holder.tvDealerTitle.text = "for ${orderList[position].dealerName}"

        holder.tvOrderAmount.text = "Amount: ${orderList[position].grandTotalAmount}"
        holder.tvOrderStatus.text = orderList[position].orderStatus

        holder.tvCreatedTime.text = Convert.FormatDateTime(orderList[position].createdOn, "dd MMM, yyyy")
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerViewScrollListener() {
            override fun onScrolledToBottom() {
                listener.OnScrolledToBottom()
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) { // Scroll Down
                    listener.OnScrollDown()
                } else if (dy < 0) { // Scroll Up
                    listener.OnScrollUp()
                }
            }
        })
    }

    interface EventListener {
        fun OnItemClicked(order: OrderObject)
        fun OnScrolledToBottom()
        fun OnScrollDown()
        fun OnScrollUp()
    }
}