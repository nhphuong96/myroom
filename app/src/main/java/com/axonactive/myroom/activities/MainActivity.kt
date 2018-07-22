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
import com.axonactive.myroom.db.DatabaseHelper
import com.axonactive.myroom.models.Room
import com.axonactive.myroom.models.RoomHolder
import com.axonactive.myroom.utils.DateUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val db : DatabaseHelper = DatabaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialzeActionBar()
        createCustomerListView()
    }

    override fun onResume() {
        super.onResume()
        createCustomerListView()
    }

    private fun createCustomerListView() {
        rv_room_list.layoutManager = LinearLayoutManager(this)
        rv_room_list.adapter = RoomAdapter(getRooms(), this)
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

    private fun getRooms() : ArrayList<Room> {
        var rooms : ArrayList<Room> = ArrayList<Room>()
        val roomsInDb = db.getAllRooms()
        for (i in roomsInDb.indices) {
            val holders = db.getHolderByRoomId(roomsInDb[i].roomId)
            var roomHoldersView : ArrayList<RoomHolder> = ArrayList<RoomHolder>()
            for (holder in holders) {
                val roomHolder = RoomHolder(holder.holderId!!, holder.fullName, holder.phoneNumber, holder.profileImage, holder.birthDate, holder.idCard,
                        holder.isOwner)
                roomHoldersView.add(roomHolder)
            }
            rooms.add(Room(roomsInDb[i].roomId!! ,roomsInDb[i].roomName, roomsInDb[i].paymentStatus , roomHoldersView))
        }
        return rooms
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
