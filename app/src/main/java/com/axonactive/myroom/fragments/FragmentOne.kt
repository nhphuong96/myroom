package com.axonactive.myroom.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import com.axonactive.myroom.R
import com.axonactive.myroom.adapters.AttributeAdapter
import com.axonactive.myroom.models.RoomAttribute

/**
 * Created by Phuong Nguyen on 6/2/2018.
 */
class FragmentOne : Fragment() {

    companion object {
        fun newInstance() : FragmentOne = FragmentOne()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater!!.inflate(R.layout.fragment_room_attributes, container, false)
        super.onCreate(savedInstanceState)

        val attributes : ArrayList<RoomAttribute> = ArrayList()
        attributes.add(RoomAttribute("Electricity", "ic_electric_red"))
        attributes.add(RoomAttribute("Water", "ic_water_drop_red"))
        attributes.add(RoomAttribute("Internet", "ic_wifi_red"))
        attributes.add(RoomAttribute("Cab", "ic_television_red"))
        attributes.add(RoomAttribute("Parking", "ic_parked_car_red"))

        val gridView : GridView = view.findViewById<GridView>(R.id.gv_room_attributes)
        gridView.numColumns = 3
        gridView.adapter = (AttributeAdapter(this.activity, attributes))

        return view
    }





}