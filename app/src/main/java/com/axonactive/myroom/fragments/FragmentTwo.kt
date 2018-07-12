package com.axonactive.myroom.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.axonactive.myroom.R
import com.axonactive.myroom.adapters.HolderManagementAdapter
import com.axonactive.myroom.adapters.RoomAdapter
import com.axonactive.myroom.core.Constants
import com.axonactive.myroom.db.DatabaseHelper
import com.axonactive.myroom.db.model.Holder
import com.axonactive.myroom.models.RoomHolder
import kotlinx.android.synthetic.main.fragment_room_holders.*

/**
 * Created by Phuong Nguyen on 6/2/2018.
 */
class FragmentTwo : Fragment() {
    private lateinit var db : DatabaseHelper
    private lateinit var holder_management_list : RecyclerView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?):
            View? {
        return inflater!!.inflate(R.layout.fragment_room_holders, container, false)
    }

    companion object {
        fun newInstance() : FragmentTwo {
            return FragmentTwo()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = DatabaseHelper(activity)
        holder_management_list = activity.findViewById<RecyclerView>(R.id.holder_management_list)
        val roomId : Long = activity.intent.getLongExtra(Constants.ROOM_ID_EXTRA, 0L)
        if (roomId != 0L)
        {
            fetchHoldersInRoom(roomId)
        }

    }

    private fun fetchHoldersInRoom(roomId : Long) {
        val holders = db.getHolderByRoomId(roomId)
        var holdersView = ArrayList<RoomHolder>()
        if (holders.isNotEmpty()) {
            for (i in holders) {
                holdersView.add(RoomHolder(i.holderId!!, i.fullName,i.phoneNumber, i.profileImage, null, null))
            }
            holder_management_list.layoutManager = LinearLayoutManager(activity)
            holder_management_list.adapter = HolderManagementAdapter(holdersView, activity)
        }

    }
}