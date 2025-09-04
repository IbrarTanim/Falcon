package com.teknopole.track3rdeye.MVP.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.teknopole.track3rdeye.ObjectModels.DealerObject
import com.teknopole.track3rdeye.R


/**
 * Created by Md. Abdur Rouf -03 on 5/15/2018.
 */
class DealerListRecyclerAdapter(private var dealerList: List<DealerObject>, private val listener: EventListener) : RecyclerView.Adapter<DealerListRecyclerAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDealerName = view.findViewById<TextView>(R.id.tvDealerName)!!
        val tvDealerAddress = view.findViewById<TextView>(R.id.tvDealerAddress)!!
        val tvDealerPhone = view.findViewById<TextView>(R.id.tvDealerPhone)!!
        val tvDealerMobile = view.findViewById<TextView>(R.id.tvDealerMobile)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.template_dealer_list, parent, false)
        val vh = ViewHolder(v)

        v.setOnClickListener {
            listener.OnItemClicked(dealerList[vh.adapterPosition])
        }
        return vh
    }

    override fun getItemCount(): Int {
        return dealerList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvDealerName.text = dealerList[position].dealerName
        holder.tvDealerAddress.text = dealerList[position].dealerAddress
        holder.tvDealerPhone.text = dealerList[position].dealerPhone
        holder.tvDealerMobile.text = dealerList[position].dealerMobile

        if(dealerList[position].dealerPhone.isEmpty())
            holder.tvDealerPhone.visibility = View.GONE
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
        fun OnItemClicked(task: DealerObject)
        fun OnScrolledToBottom()
        fun OnScrollDown()
        fun OnScrollUp()

    }
}