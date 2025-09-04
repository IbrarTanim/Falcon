package com.teknopole.track3rdeye.MVP.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.teknopole.track3rdeye.ObjectModels.ProductDetail
import com.teknopole.track3rdeye.ObjectModels.ProductObject
import com.teknopole.track3rdeye.R


/**
 * Created by Md. Abdur Rouf -03 on 5/15/2018.
 */
class ProductDetailsListRecyclerAdapter(private var productDetailsList: List<ProductDetail>) : RecyclerView.Adapter<ProductDetailsListRecyclerAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDetailLabel = view.findViewById<TextView>(R.id.tvDetailLabel)!!
        val tvDetailModel = view.findViewById<TextView>(R.id.tvDetailModel)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.template_product_profile_details, parent, false)
        val vh = ViewHolder(v)

//        v.setOnClickListener {
//            listener.OnItemClicked(productDetailsList[vh.adapterPosition])
//        }
        return vh
    }

    override fun getItemCount(): Int {
        return productDetailsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvDetailLabel.text = productDetailsList[position].label
        holder.tvDetailModel.text = productDetailsList[position].model

    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {

    }

    interface EventListener {
        fun OnItemClicked(product: ProductObject)
    }
}