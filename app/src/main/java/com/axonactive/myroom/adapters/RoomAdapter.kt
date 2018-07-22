package com.axonactive.myroom.adapters

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.AsyncTask
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SwitchCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.axonactive.myroom.R
import com.axonactive.myroom.activities.BillCalculatorActivity
import com.axonactive.myroom.activities.RoomManagementActivity
import com.axonactive.myroom.core.Constants
import com.axonactive.myroom.db.DatabaseHelper
import com.axonactive.myroom.models.Room
import com.axonactive.myroom.models.RoomHolder
import de.hdodenhof.circleimageview.CircleImageView
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.roomholder_list_item.view.*

/**
 * Created by Phuong Nguyen on 5/11/2018.
 */
class RoomAdapter (private var items : ArrayList<Room>,
                   private val context: Context) : RecyclerView.Adapter<RoomAdapter.ViewHolder>() {

    private val db : DatabaseHelper = DatabaseHelper(context)

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.roomholder_list_item, parent, false), context)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder?, position: Int) {
        viewHolder?.tvRoomType?.text = items[position].roomName
        if (context.resources.getString(R.string.have_not_paid).equals(items[position].roomStatus)) {
            viewHolder?.tvStatus?.setBackgroundColor(ContextCompat.getColor(context, R.color.red))
        }
        else if (context.resources.getString(R.string.paid).equals(items[position].roomStatus)) {
            viewHolder?.tvStatus?.setBackgroundColor(ContextCompat.getColor(context, R.color.green))
        }
        viewHolder?.tvStatus?.text = items[position].roomStatus

        if (items[position].holders.isNotEmpty()) {
            var ownerName = "No information avaialble"
            var imageName = "ic_placeholder"
            for (i in items[position].holders) {
                if (i.isOwner == 1) {
                    ownerName = i.fullName
                    imageName = i.imageName!!
                    break
                }
            }
            viewHolder?.tvHolderName?.text = ownerName
            viewHolder?.tvTotal?.text = context.resources.getString(R.string.holders_count, items[position].holders.size)
            viewHolder?.imgRoomHolder?.setImageResource(context.resources.getIdentifier(imageName,
                    "drawable", context.packageName))
        }
        else {
            viewHolder?.tvHolderName?.text = "Room is empty"
            viewHolder?.tvTotal?.text = "0"
            viewHolder?.imgRoomHolder?.setImageResource(context.resources.getIdentifier("ic_placeholder", "drawable", context.packageName))
        }

        viewHolder?.raView?.setOnClickListener { _ ->
            val intent = Intent(context, RoomManagementActivity::class.java)
            intent.putExtra(Constants.ROOM_NAME_EXTRA, viewHolder?.tvRoomType.text.toString())
            intent.putExtra(Constants.ROOM_ID_EXTRA, items[position].roomId)
            context.startActivity(intent)
        }

        viewHolder?.raView?.isLongClickable = true
        viewHolder?.raView?.setOnLongClickListener { view: View? ->
            val builder : AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setCancelable(false)
            builder.setIcon(R.drawable.ic_alert_dialog)
            var view : View = LayoutInflater.from(context).inflate(R.layout.dialog_header_with_switch, null)
            var title : TextView = view.findViewById(R.id.textView5)
            var editable : SwitchCompat = view.findViewById(R.id.editable)
            editable.visibility = View.GONE
            title.text = "Delete room"
            builder.setCustomTitle(view)
            builder.setMessage("Do you want to delete this room?")
            builder.setNegativeButton(context.resources.getString(R.string.no), DialogInterface.OnClickListener { dialogInterface, _ ->
                dialogInterface.dismiss()
            })
            builder.setPositiveButton(context.resources.getString(R.string.yes), DialogInterface.OnClickListener { _, i ->
                if (db.deleteHoldersByRoomId(items[position].roomId) > 0
                        && db.deleteRoom(items[position].roomId) > 0
                        && db.deleteRoomAttribute(items[position].roomId, null) > 0) {
                    Toasty.success(context, String.format(context.resources.getString(R.string.deleted_room_successful), items[position].roomName), Toast.LENGTH_SHORT, true).show()
                    items.clear()
                    val roomsInDb = db.getAllRooms()
                    for (i in roomsInDb.indices) {
                        val holders = db.getHolderByRoomId(roomsInDb[i].roomId)
                        var roomHoldersView : ArrayList<RoomHolder> = ArrayList<RoomHolder>()
                        for (holder in holders) {
                            val roomHolder = RoomHolder(holder.holderId!!, holder.fullName, holder.phoneNumber, holder.profileImage, holder.birthDate, holder.idCard,
                                    holder.isOwner)
                            roomHoldersView.add(roomHolder)
                        }
                        items.add(Room(roomsInDb[i].roomId!! ,roomsInDb[i].roomName, roomsInDb[i].paymentStatus , roomHoldersView))
                    }

                }
                else {
                    Toasty.error(context, String.format(context.resources.getString(R.string.deleted_room_failed), items[position].roomName), Toast.LENGTH_LONG, true).show()
                }
                notifyDataSetChanged()
            })

            val dialog : Dialog = builder.create()
            dialog.show()
            true
        }

        viewHolder?.btnSendBill?.setOnClickListener { _ ->
            val intent = Intent(context, BillCalculatorActivity::class.java)
            intent.putExtra(Constants.ROOM_ID_EXTRA, items[position].roomId)
            context.startActivity(intent)
        }
    }

    class ViewHolder : RecyclerView.ViewHolder {
        var tvRoomType : TextView
        var tvStatus : TextView
        var tvTotal  : TextView
        var tvHolderName  : TextView
        var imgRoomHolder : CircleImageView
        var raView : View
        var btnSendBill : Button


        constructor(view : View, context : Context) : super(view){
            tvRoomType = view.tv_room_type!!
            imgRoomHolder = view.room_image
            tvHolderName = view.tv_holder_name!!
            tvTotal = view.tv_total!!
            tvStatus = view.tv_room_status!!
            raView = view
            btnSendBill = view.btn_send_bill!!
        }



    }
}