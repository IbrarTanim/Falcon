package com.teknopole.track3rdeye.MVP.Adapters

import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener
import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.ObjectModels.ProductImage
import com.teknopole.track3rdeye.ObjectModels.ProductObject
import com.teknopole.track3rdeye.R
import com.teknopole.track3rdeye.Utils.APIClient.RESTClient
import kotlinx.android.synthetic.main.fragment_home.*


/**
 * Created by Md. Abdur Rouf -03 on 5/15/2018.
 */
class ProductImagesGridRecyclerAdapter(private var productImages: List<ProductImage>) : RecyclerView.Adapter<ProductImagesGridRecyclerAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivPic = view.findViewById<ImageView>(R.id.ivPic)!!

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.template_product_profile_images, parent, false)
        val vh = ViewHolder(v)

//        v.setOnClickListener {
//            listener.OnItemClicked(productImages[vh.adapterPosition])
//        }
        return vh
    }

    override fun getItemCount(): Int {
        return productImages.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            ImageLoader.getInstance()
                    .apply {
                        init(app.GetUILConfig())
                        clearMemoryCache()
                        clearDiskCache()
                        loadImage(RESTClient.GetProductImageUrl(productImages[position].image), object : SimpleImageLoadingListener() {
                            override fun onLoadingComplete(imageUri: String?, view: View?, loadedImage: Bitmap?) {
                                try {
                                    if (loadedImage != null) {
//                                        app.StoreImageToCache("companyLogo", loadedImage)

                                        holder.ivPic.setImageBitmap(loadedImage)
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        })
                    }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {

    }

    interface EventListener {
        fun OnItemClicked(product: ProductObject)

    }
}