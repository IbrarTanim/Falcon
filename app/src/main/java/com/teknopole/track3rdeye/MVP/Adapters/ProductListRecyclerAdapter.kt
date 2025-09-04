package com.teknopole.track3rdeye.MVP.Adapters

import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.teknopole.track3rdeye.ObjectModels.OrderProductDetail
import com.teknopole.track3rdeye.ObjectModels.ProductObject
import com.teknopole.track3rdeye.R


/**
 * Created by Md. Abdur Rouf -03 on 5/15/2018.
 */
class ProductListRecyclerAdapter(private var productList: List<OrderProductDetail>, private val listener: EventListener) : RecyclerView.Adapter<ProductListRecyclerAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvProductName = view.findViewById<TextView>(R.id.tvProductName)!!
        val tvProductCode = view.findViewById<TextView>(R.id.tvProductCode)!!
        

        val tvPrice = view.findViewById<TextView>(R.id.tvPrice)!!
        val tvQuantity = view.findViewById<TextView>(R.id.tvQuantity)!!
        val tvStock = view.findViewById<TextView>(R.id.tvStock)!!
        val tvAmount = view.findViewById<TextView>(R.id.tvAmount)!!

        val btnDelete = view.findViewById<ImageButton>(R.id.btnDelete)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.template_product_list, parent, false)
        val vh = ViewHolder(v)

        v.setOnClickListener {
            listener.OnItemClicked(productList[vh.adapterPosition])
        }

        vh.btnDelete.setOnClickListener {
            listener.OnItemDeleted(vh.adapterPosition)
        }
        return vh
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvProductName.text = productList[position].productName
        holder.tvProductCode.text = productList[position].productCode

        holder.tvPrice.text = productList[position].salesPrice.toString()

        holder.tvStock.text = productList[position].currentStockQty.toString()

        holder.tvQuantity.text = productList[position].orderQty.toString()
        holder.tvAmount.text = productList[position].totalAmount.toString()

        holder.tvQuantity.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    val quantity = Integer.parseInt(s.toString())

                    productList[holder.adapterPosition].orderQty = quantity.toDouble();
                    productList[holder.adapterPosition].totalAmount = (quantity * productList[holder.adapterPosition].salesPrice).toDouble()

                    holder.tvAmount.text = "" + (quantity * productList[holder.adapterPosition].salesPrice)
                } catch (e: NumberFormatException) {
//                    e.printStackTrace()
                    holder.tvAmount.text = "0"
                }

                listener.onAmountChanged()
            }


        })


    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {

    }

    interface EventListener {
        fun OnItemClicked(product: OrderProductDetail)
        fun OnItemDeleted(position: Int)
        fun onAmountChanged()

    }
}