package com.axonactive.myroom.activities

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.axonactive.myroom.R
import com.axonactive.myroom.adapters.RoomAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var rooms: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initialzeActionBar()

        addRooms()

        rv_room_list.layoutManager = LinearLayoutManager(this)

        rv_room_list.adapter = RoomAdapter(rooms, this)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater?.inflate(R.menu.menu_on_action_bar, menu)
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
        rooms.add("Room 1")
        rooms.add("Room 2")
        rooms.add("Room 3")
        rooms.add("Room 4")
    }

    private fun initialzeActionBar() {
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setTitle("My Rooms")
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.blue)))
    }
}
