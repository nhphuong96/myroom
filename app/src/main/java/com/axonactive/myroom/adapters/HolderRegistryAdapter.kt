package com.axonactive.myroom.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.axonactive.myroom.R
import com.axonactive.myroom.models.RoomHolder
import kotlinx.android.synthetic.main.activity_sign_up.view.*
import kotlinx.android.synthetic.main.roomholder_registry_list_item.view.*

/**
 * Created by Phuong Nguyen on 5/16/2018.
 */
class HolderRegistryAdapter (val items : ArrayList<RoomHolder>, val context: Context) : RecyclerView.Adapter<HolderRegistryAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size;
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): HolderRegistryAdapter.ViewHolder {
        return HolderRegistryAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.roomholder_registry_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: HolderRegistryAdapter.ViewHolder?, position: Int) {
        holder?.tvHolderPartnerName?.text = items.get(position).fullName;
        holder?.tvHolderPartnerPhone?.text = items.get(position).phoneNumber;
        holder?.ivProfileImage?.setImageResource(items.get(position).image);
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val tvHolderPartnerName = view.tv_holder_partner_name_label;
        val tvHolderPartnerPhone = view.tv_holder_partner_phone_label;
        val ivProfileImage = view.iv_partner_profile_image;
    }
}