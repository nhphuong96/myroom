package com.axonactive.myroom.activities

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.axonactive.myroom.R
import com.axonactive.myroom.adapters.RoomAdapter
import com.axonactive.myroom.models.Room
import com.axonactive.myroom.models.RoomHolder
import com.axonactive.myroom.utils.DateUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var rooms: ArrayList<Room> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialzeActionBar()
        addRooms()
        createCustomerListView()
    }

    private fun createCustomerListView() {
        rv_room_list.layoutManager = LinearLayoutManager(this)
        rv_room_list.adapter = RoomAdapter(rooms, this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater?.inflate(R.menu.menu_on_main_action_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId){
        R.id.addRoomHolder -> {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun addRooms() {
        val holders : ArrayList<RoomHolder> = ArrayList<RoomHolder>()
        holders.add(RoomHolder("Phuong Nguyen",
                "0902444505",
                "boy", DateUtils.toSimpleDate(
                "16/03/1996"),
                "025389977"))
        rooms.add(Room("Room 1",resources.getString(R.string.have_not_paid), holders))
        val holders2 : ArrayList<RoomHolder> = ArrayList<RoomHolder>()
        holders2.add(RoomHolder("Tien Vu",
                "01222696928",
                "girl4",
                DateUtils.toSimpleDate("08/12/1996"),
                ""))
        rooms.add(Room("Room 2", resources.getString(R.string.paid), holders2))
    }

    private fun initialzeActionBar() {
        var toolbar : Toolbar = findViewById<Toolbar>(R.id.toolbar)
        var title : TextView = toolbar.findViewById<TextView>(R.id.toolbar_title)
        title.text = "My Rooms"
        title.textSize = 14f
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.blue)))
    }
}
