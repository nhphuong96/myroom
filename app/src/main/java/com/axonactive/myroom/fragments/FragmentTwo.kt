package com.axonactive.myroom.fragments

import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.axonactive.myroom.R
import com.axonactive.myroom.adapters.HolderManagementAdapter
import com.axonactive.myroom.core.Constants
import com.axonactive.myroom.db.DatabaseHelper
import com.axonactive.myroom.models.RoomHolder
import com.victor.loading.rotate.RotateLoading

/**
 * Created by Phuong Nguyen on 6/2/2018.
 */
class FragmentTwo : Fragment() {
    private lateinit var db : DatabaseHelper
    private lateinit var holderManagementList : RecyclerView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?):
            View? {
        var view : View = inflater!!.inflate(R.layout.fragment_room_holders, container, false)
        holderManagementList = view!!.findViewById<RecyclerView>(R.id.holder_management_list)
        return view
    }

    companion object {
        fun newInstance() : FragmentTwo {
            return FragmentTwo()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = DatabaseHelper(activity)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val roomId : Long = activity.intent.getLongExtra(Constants.ROOM_ID_EXTRA, 0L)
        fetchHoldersInRoom(roomId)
    }

    private fun fetchHoldersInRoom(roomId : Long) {
        val holders = db.getHolderByRoomId(roomId)
        var holdersView = ArrayList<RoomHolder>()
        if (holders.isNotEmpty()) {
            for (i in holders) {
                holdersView.add(RoomHolder(i.holderId!!, i.fullName,i.phoneNumber, i.profileImage, null, null, i.isOwner))
            }
            holderManagementList.layoutManager = LinearLayoutManager(activity)
            holderManagementList.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
            holderManagementList.adapter = HolderManagementAdapter(holdersView, activity)
        }

    }

//    inner class AsyncTaskRunner : AsyncTask<Long, Unit, Unit>() {
//
//        private lateinit var rotateLoading : RotateLoading
//
//        override fun onProgressUpdate(vararg values: Unit?) {
//
//        }
//
//        override fun doInBackground(vararg roomId: Long?): Unit {
//            if (roomId != null)
//            {
//                fetchHoldersInRoom(roomId[0]!!)
//            }
//        }
//
//        override fun onPreExecute() {
//            rotateLoading = activity.findViewById<RotateLoading>(R.id.rotateloading)
//            rotateLoading.start()
//        }
//
//        override fun onPostExecute(result: Unit?) {
//            if (rotateLoading.isStart) {
//                rotateLoading.stop()
//            }
//        }
//    }
}