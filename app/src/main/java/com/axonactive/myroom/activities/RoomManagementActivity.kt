package com.axonactive.myroom.activities

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.axonactive.myroom.R
import com.axonactive.myroom.adapters.RoomManagementPagerAdapter
import com.axonactive.myroom.core.Constants
import com.axonactive.myroom.validation.Validator

/**
 * Created by Phuong Nguyen on 6/1/2018.
 */
class RoomManagementActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_management)
        initializeActionBar(intent.getStringExtra(Constants.ROOM_NAME_EXTRA))
        initViewPager()
    }

    private fun initViewPager() {
        val tabLayout : TabLayout = findViewById(R.id.tab_layout)
        val viewPager : ViewPager = findViewById(R.id.viewpager)
        val adapter = RoomManagementPagerAdapter(supportFragmentManager)

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.getTabAt(0)!!.setIcon(R.drawable.ic_list_property)
        tabLayout.getTabAt(1)!!.setIcon(R.drawable.ic_people)
        tabLayout.getTabAt(2)!!.setIcon(R.drawable.ic_bar_chart)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    private fun initializeActionBar(title : String) {
        val toolbar : Toolbar = findViewById<Toolbar>(R.id.toolbar)
        val tvTitle : TextView = toolbar.findViewById<TextView>(R.id.toolbar_title)
        tvTitle.text = title
        tvTitle.textSize = 14f
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_backspace))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.blue)))
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}