package com.axonactive.myroom.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import com.axonactive.myroom.R
import com.axonactive.myroom.adapters.AttributeAdapter
import com.axonactive.myroom.core.Constants
import com.axonactive.myroom.db.DatabaseHelper
import com.axonactive.myroom.db.model.Attribute

/**
 * Created by Phuong Nguyen on 6/2/2018.
 */
class FragmentOne : Fragment() {
    private lateinit var db : DatabaseHelper

    companion object {
        fun newInstance() : FragmentOne = FragmentOne()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = DatabaseHelper(activity)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        val view : View = inflater!!.inflate(R.layout.fragment_room_attributes, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val roomId : Long = activity.intent.getLongExtra(Constants.ROOM_ID_EXTRA, 0L)
        val attributesInDb = db.getAllAttributes()
        val attributes = ArrayList<Attribute>()
        for (att in attributesInDb) {
            attributes.add(att)
        }
        val gridView : GridView = view!!.findViewById<GridView>(R.id.gv_room_attributes)
        gridView.numColumns = 3
        gridView.adapter = (AttributeAdapter(this.activity, com.axonactive.myroom.models.RoomAttribute(roomId, attributes)))
    }





}