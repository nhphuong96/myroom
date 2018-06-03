package com.axonactive.myroom.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.axonactive.myroom.R

/**
 * Created by Phuong Nguyen on 6/2/2018.
 */
class FragmentTwo : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?):
            View? = inflater!!.inflate(R.layout.fragment_room_holders, container, false)

    companion object {
        fun newInstance() : FragmentTwo = FragmentTwo()
    }
}