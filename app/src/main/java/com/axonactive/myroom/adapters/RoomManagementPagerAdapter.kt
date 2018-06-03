package com.axonactive.myroom.adapters

import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.axonactive.myroom.fragments.FragmentOne
import com.axonactive.myroom.fragments.FragmentThree
import com.axonactive.myroom.fragments.FragmentTwo

/**
 * Created by Phuong Nguyen on 6/1/2018.
 */
class RoomManagementPagerAdapter(fragmentManager : FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment? = when (position) {
        0 -> FragmentOne.newInstance()
        1 -> FragmentTwo.newInstance()
        2 -> FragmentThree.newInstance()
        else -> null
    }

    override fun getCount(): Int = 3

}