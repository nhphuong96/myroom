package com.axonactive.myroom.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.axonactive.myroom.R
import kotlinx.android.synthetic.main.roomholder_list_item.view.*

/**
 * Created by Phuong Nguyen on 5/11/2018.
 */

class RoomAdapter (val items : ArrayList<String>, val context: Context) : RecyclerView.Adapter<RoomAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.roomholder_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.tvRoomType?.text = items.get(position)
    }

    class ViewHolder (view : View) : RecyclerView.ViewHolder(view) {
        val tvRoomType = view.tv_room_type;
    }
}