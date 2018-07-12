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
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.roomholder_list_item.view.*

/**
 * Created by Phuong Nguyen on 5/11/2018.
 */
class RoomAdapter (private val items : ArrayList<Room>,
                   private val context: Context) : RecyclerView.Adapter<RoomAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.roomholder_list_item, parent, false), context)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder?, position: Int) {
        viewHolder?.tvRoomType?.text = items[position].roomName
        if (context.resources.getString(R.string.have_not_paid).equals(items[position].roomStatus)) {
            viewHolder?.tvStatus?.setBackgroundColor(ContextCompat.getColor(context, R.color.red))
        }
        else if (context.resources.getString(R.string.paid).equals(items[position].roomStatus)) {
            viewHolder?.tvStatus?.setBackgroundColor(ContextCompat.getColor(context, R.color.green))
        }
        viewHolder?.tvStatus?.text = items[position].roomStatus

        if (items[position].holders.isNotEmpty()) {
            viewHolder?.tvHolderName?.text = items[position].holders[0].fullName
            viewHolder?.tvTotal?.text = context.resources.getString(R.string.holders_count, items[position].holders.size)
            viewHolder?.imgRoomHolder?.setImageResource(context.resources.getIdentifier(items[position].holders[0].imageName,
                    "drawable", context.packageName))
        }
        else {
            viewHolder?.tvHolderName?.text = "Room is empty"
            viewHolder?.tvTotal?.text = "0"
            viewHolder?.imgRoomHolder?.setImageResource(context.resources.getIdentifier("ic_placeholder", "drawable", context.packageName))
        }

        viewHolder?.raView?.setOnClickListener { view ->
            val intent = Intent(context, RoomManagementActivity::class.java)
            intent.putExtra(Constants.ROOM_NAME_EXTRA, viewHolder?.tvRoomType.text.toString())
            intent.putExtra(Constants.ROOM_ID_EXTRA, items[position].roomId)
            context.startActivity(intent)
        }
    }

    class ViewHolder : RecyclerView.ViewHolder {
        var tvRoomType : TextView
        var tvStatus : TextView
        var tvTotal  : TextView
        var tvHolderName  : TextView
        var imgRoomHolder : CircleImageView
        var raView : View


        constructor(view : View, context : Context) : super(view){
            tvRoomType = view.tv_room_type!!
            imgRoomHolder = view.room_image
            tvHolderName = view.tv_holder_name!!
            tvTotal = view.tv_total!!
            tvStatus = view.tv_room_status!!
            raView = view
        }



    }
}