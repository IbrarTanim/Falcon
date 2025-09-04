package com.teknopole.track3rdeye.MVP.Adapters

import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener
import com.teknopole.track3rdeye.App.app
import com.teknopole.track3rdeye.ObjectModels.ContactPerson
import com.teknopole.track3rdeye.PopupAndDialogs.DropDownListPopUp
import com.teknopole.track3rdeye.R
import com.teknopole.track3rdeye.Utils.APIClient.RESTClient
import com.teknopole.track3rdeye.Utils.Convert
import de.hdodenhof.circleimageview.CircleImageView


/**
 * Created by Md. Abdur Rouf -03 on 5/15/2018.
 */
class DealerContactPersonListRecyclerAdapter(private var dealerId :Int, private var contactPersonList: List<ContactPerson>, private var editModeListener: EventListener?) : RecyclerView.Adapter<DealerContactPersonListRecyclerAdapter.ViewHolder>() {
    private var dropDownLisPopup: DropDownListPopUp? = null

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName = view.findViewById<TextView>(R.id.tvDealerName)!!
        val tvNameLabel = view.findViewById<TextView>(R.id.tvNameLabel)!!

        val tvDesignation = view.findViewById<TextView>(R.id.tvDesignation)!!
        val tvDesignationLabel = view.findViewById<TextView>(R.id.tvDesignationLabel)!!

        val tvGender = view.findViewById<TextView>(R.id.tvGender)!!
        val tvEmail = view.findViewById<TextView>(R.id.tvEmail)!!
        val tvMobile = view.findViewById<TextView>(R.id.tvMobile)!!
        val tvPhone = view.findViewById<TextView>(R.id.tvPhone)!!
        val ivContactPerson = view.findViewById<CircleImageView>(R.id.ivContactPerson)!!
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)!!

        val btnEditPic = view.findViewById<ImageButton>(R.id.btnEditPic)!!
        val btnSubmit = view.findViewById<Button>(R.id.btnSubmit)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.template_dealer_contact_person_list, parent, false)
        val holder = ViewHolder(v)

        if (editModeListener!=null) {
            val padding = Convert.DpToPixel(8)

            holder.itemView.background
            holder.tvNameLabel.visibility = View.VISIBLE
            holder.tvDesignationLabel.visibility = View.VISIBLE

            holder.tvName.apply {

                isEnabled = true
                setPadding(padding, padding, padding, padding)
            }

            holder.tvDesignation.apply {
                isEnabled = true
                setPadding(padding, padding, padding, padding)
            }
            holder.tvEmail.apply {
                isEnabled = true
                setPadding(padding, padding, padding, padding)
            }
            holder.tvMobile.apply {
                isEnabled = true
                setPadding(padding, padding, padding, padding)
            }
            holder.tvPhone.apply {
                isEnabled = true
                setPadding(padding, padding, padding, padding)
            }

            holder.tvGender.apply {
                isEnabled = true
                setPadding(padding, padding, padding, padding)
            }

            holder.tvGender.setOnClickListener {
                dropDownLisPopup = DropDownListPopUp(holder.itemView.context, arrayListOf("Male", "Female"), object : DropDownListPopUp.ItemSelectListener {
                    override fun OnItemSelected(actionText: String) {
                        holder.tvGender.setText(actionText)
                    }
                })
                dropDownLisPopup?.Show(holder.tvGender)
            }

            holder.btnEditPic.visibility = View.VISIBLE
            holder.btnEditPic.setOnClickListener {
                editModeListener!!.OnSubmitEditPicClicked(GetContactPersonDetailsFromView(holder))
            }

            holder.btnSubmit.visibility = View.VISIBLE

            holder.btnSubmit.setOnClickListener {
                editModeListener!!.OnContactPersonEditClicked(GetContactPersonDetailsFromView(holder))
            }
        }
        return holder
    }

    private fun GetContactPersonDetailsFromView(holder: ViewHolder): ContactPerson {

        val contactPerson = ContactPerson()
        try {
            contactPerson.id = contactPersonList[holder.adapterPosition].id
            contactPerson.companyId = app.GetUserSession().cpProfileId
            contactPerson.dealerId = dealerId
            contactPerson.employeeId = app.GetUserSession().id

            contactPerson.contactPersonName = holder.tvName.text.toString()
            contactPerson.contactPersonEmail = holder.tvEmail.text.toString()
            contactPerson.contactPersonMobile = holder.tvMobile.text.toString()
            contactPerson.contactPersonPhone = holder.tvPhone.text.toString()
            contactPerson.contactPersonDesignation = holder.tvDesignation.text.toString()
            contactPerson.contactPersonGender = holder.tvGender.text.toString()

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return contactPerson
    }

    override fun getItemCount(): Int {
        return contactPersonList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvName.text = contactPersonList[position].contactPersonName
        holder.tvDesignation.text = contactPersonList[position].contactPersonDesignation
        holder.tvGender.setText(contactPersonList[position].contactPersonGender)
        holder.tvEmail.setText(contactPersonList[position].contactPersonEmail)
        holder.tvMobile.setText(contactPersonList[position].contactPersonMobile)
        holder.tvPhone.setText(contactPersonList[position].contactPersonPhone)

        try {
            ImageLoader.getInstance()
                    .apply {
                        init(app.GetUILConfig())
                        clearMemoryCache()
                        clearDiskCache()
                        loadImage(RESTClient.GetContactPersonImageUrl(contactPersonList[position].contactPersonPhoto), object : SimpleImageLoadingListener() {
                            override fun onLoadingComplete(imageUri: String?, view: View?, loadedImage: Bitmap?) {
                                try {
                                    if (loadedImage != null) {
//                                        app.StoreImageToCache("companyLogo", loadedImage)
                                        holder.progressBar.visibility = View.GONE
                                        holder.btnEditPic.visibility = View.GONE
                                        holder.ivContactPerson.setImageBitmap(loadedImage)
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

    interface EventListener {
        fun OnContactPersonEditClicked(contactPerson: ContactPerson)
        fun OnSubmitEditPicClicked(contactPerson: ContactPerson)
    }


}