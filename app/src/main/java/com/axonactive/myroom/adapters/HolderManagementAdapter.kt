package com.axonactive.myroom.adapters

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.axonactive.myroom.R
import com.axonactive.myroom.activities.RoomManagementActivity
import com.axonactive.myroom.core.Constants
import com.axonactive.myroom.models.Room
import com.axonactive.myroom.models.RoomHolder
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_sign_up.view.*
import kotlinx.android.synthetic.main.holder_management_item.view.*
import kotlinx.android.synthetic.main.roomholder_list_item.view.*

/**
 * Created by Phuong Nguyen on 7/3/2018.
 */
class HolderManagementAdapter(private val items : ArrayList<RoomHolder>,
                               private val context: Context) : RecyclerView.Adapter<HolderManagementAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.holder_management_item, parent, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder?, position: Int) {
        viewHolder?.tvHolderName?.text = items[position].fullName
        viewHolder?.tvHolderPhone?.text = items[position].phoneNumber
        viewHolder?.imgRoomHolder?.setImageResource(context.resources.getIdentifier(items[position].imageName,
                "drawable", context.packageName))

    }

    class ViewHolder : RecyclerView.ViewHolder {
        var tvHolderName: TextView
        var tvHolderPhone: TextView
        var imgRoomHolder: CircleImageView


        constructor(view: View) : super(view) {
            tvHolderPhone = view.holder_management_phone!!
            imgRoomHolder = view.holder_management_profile_image!!
            tvHolderName = view.holder_management_name!!

        }
    }
}