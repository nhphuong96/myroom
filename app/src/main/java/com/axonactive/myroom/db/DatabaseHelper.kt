package com.axonactive.myroom.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.axonactive.myroom.db.model.Holder
import com.axonactive.myroom.db.model.Room
import com.axonactive.myroom.utils.DateUtils
import com.axonactive.myroom.utils.RoomStatus
import java.util.*


/**
 * Created by Phuong Nguyen on 6/13/2018.
 */
class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DatabaseHelper.DB_NAME, null, DatabaseHelper.DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        createTable(db, TABLE_HOLDER, listOf(FULL_NAME, PHONE_NUMER, BIRTHDAY, ID_CARD, ADDRESS, PROFILE_IMAGE, ROOM_ID)
                                    , listOf(TEXT, TEXT, TEXT, TEXT, TEXT, TEXT, INTEGER))
        createTable(db, TABLE_ROOM, listOf(ROOM_NAME, PAYMENT_STATUS),
                                    listOf(TEXT, TEXT))
    }

    private fun createTable(db: SQLiteDatabase, tableName: String, headers : List<String>, headerTypes : List<String>)
    {
        val builder = StringBuilder("CREATE TABLE ")
        if (!isInitializeDB) {
            builder.append(" IF NOT EXISTS ")
        }
        builder.append(tableName).append("(").append(DatabaseHelper.ID).append(" INTEGER PRIMARY KEY,")
        for (i in headers.indices) {
            builder.append(headers[i]).append(" ").append(headerTypes[i]).append(",")
        }
        builder.append(CREATED_AT).append(" DATETIME)")
        db.execSQL(builder.toString())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOLDER)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROOM)
        onCreate(db)
    }

    //----------------------------------------HOLDER - CRUD --------------------------------------//

    fun createHolder(holder : Holder) : Long {
        val db : SQLiteDatabase = this.writableDatabase

        val values : ContentValues = ContentValues()
        values.put(FULL_NAME, holder.fullName)
        values.put(PHONE_NUMER, holder.phoneNumber)
        if (holder.birthDate != null) {
            values.put(BIRTHDAY, DateUtils.toSimpleDateString(holder.birthDate))
        }
        values.put(ID_CARD, holder.idCard)
        values.put(ADDRESS, holder.address)
        values.put(PROFILE_IMAGE, holder.profileImage)
        values.put(CREATED_AT, DateUtils.toSimpleDateString(Date()))
        values.put(ROOM_ID, holder.roomId)

        return db.insert(TABLE_HOLDER, null, values)
    }

    fun getHolder(holderId : Long) : Holder {
        val db : SQLiteDatabase = this.writableDatabase

        val selectQuery = "SELECT * FROM " + TABLE_HOLDER + " WHERE " + ID + " = " + holderId

        Log.e(LOG, selectQuery)

        val cursor : Cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            cursor.moveToFirst()
        }
        return Holder(cursor.getLong(cursor.getColumnIndex(ID)),
                    cursor.getString(cursor.getColumnIndex(FULL_NAME)),
                    cursor.getString(cursor.getColumnIndex(PHONE_NUMER)),
                    cursor.getString(cursor.getColumnIndex(ID_CARD)),
                    DateUtils.toSimpleDate(cursor.getString(cursor.getColumnIndex(BIRTHDAY))),
                    cursor.getString(cursor.getColumnIndex(ADDRESS)),
                    cursor.getString(cursor.getColumnIndex(PROFILE_IMAGE)),
                    cursor.getString(cursor.getColumnIndex(CREATED_AT)),
                    cursor.getLong(cursor.getColumnIndex(ROOM_ID)))
    }

    fun getHolderByRoomId(roomId : Long?) : List<Holder> {
        val result : ArrayList<Holder> = ArrayList<Holder>()
        val selectHolderByRoomId = "SELECT * FROM " + TABLE_HOLDER + " WHERE " + ROOM_ID + " = ?"
        Log.e(LOG, selectHolderByRoomId)

        val db : SQLiteDatabase = this.writableDatabase
        val c : Cursor = db.rawQuery(selectHolderByRoomId, arrayOf(roomId.toString()))

        if (c.moveToFirst()) {
            do {
                val holder = Holder(c.getLong(c.getColumnIndex(ID)),
                        c.getString(c.getColumnIndex(FULL_NAME)),
                        c.getString(c.getColumnIndex(PHONE_NUMER)),
                        c.getString(c.getColumnIndex(ID_CARD)),
                        DateUtils.toSimpleDate(c.getString(c.getColumnIndex(BIRTHDAY))),
                        c.getString(c.getColumnIndex(ADDRESS)),
                        c.getString(c.getColumnIndex(PROFILE_IMAGE)),
                        c.getString(c.getColumnIndex(CREATED_AT)),
                        c.getLong(c.getColumnIndex(ROOM_ID)))
                result.add(holder)
            } while (c.moveToNext())
        }
        return result
    }

    fun getAllHolders() : List<Holder> {
        val result : ArrayList<Holder> = ArrayList<Holder>()
        val selectAllQuery = "SELECT * FROM " + TABLE_HOLDER
        Log.e(LOG, selectAllQuery)

        val db : SQLiteDatabase = this.writableDatabase
        val c : Cursor = db.rawQuery(selectAllQuery, null)

        if (c.moveToFirst()) {
            do {
                val holder = Holder(c.getLong(c.getColumnIndex(ID)),
                        c.getString(c.getColumnIndex(FULL_NAME)),
                        c.getString(c.getColumnIndex(PHONE_NUMER)),
                        c.getString(c.getColumnIndex(ID_CARD)),
                        DateUtils.toSimpleDate(c.getString(c.getColumnIndex(BIRTHDAY))),
                        c.getString(c.getColumnIndex(ADDRESS)),
                        c.getString(c.getColumnIndex(PROFILE_IMAGE)),
                        c.getString(c.getColumnIndex(CREATED_AT)),
                        c.getLong(c.getColumnIndex(ROOM_ID)))
                result.add(holder)
            } while (c.moveToNext())
        }
        return result
    }

    fun updateHolder(holder : Holder) : Int {
        val db : SQLiteDatabase = this.writableDatabase

        val values : ContentValues = ContentValues()
        values.put(FULL_NAME, holder.fullName)
        values.put(PHONE_NUMER, holder.phoneNumber)
        values.put(BIRTHDAY, DateUtils.toSimpleDateString(holder.birthDate))
        values.put(ID_CARD, holder.idCard)
        values.put(ADDRESS, holder.address)
        values.put(PROFILE_IMAGE, holder.profileImage)
        values.put(ROOM_ID, holder.roomId)

        return db.update(TABLE_HOLDER, values, ID + " = ?", arrayOf(holder.holderId.toString()))
    }

    fun deleteHolder(holderId : Long) {
        val db : SQLiteDatabase = this.writableDatabase
        db.delete(TABLE_HOLDER, ID + " = ?", arrayOf(holderId.toString()))
    }

    //----------------------------------------ROOM - CRUD -------------------------------------//
    fun createRoom(room : Room) : Long {
        val db : SQLiteDatabase = this.writableDatabase

        val values : ContentValues = ContentValues()
        values.put(ROOM_NAME, room.roomName)
        values.put(PAYMENT_STATUS, room.paymentStatus)
        values.put(CREATED_AT, DateUtils.toSimpleDateString(Date()))

        return db.insert(TABLE_ROOM, null, values)
    }

    fun getRoom(roomId : Long) : Room {
        val db : SQLiteDatabase = this.writableDatabase

        val selectQuery = "SELECT * FROM " + TABLE_ROOM + " WHERE " + ID + " = " + roomId

        Log.e(LOG, selectQuery)

        val cursor : Cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            cursor.moveToFirst()
        }
        return Room(cursor.getLong(cursor.getColumnIndex(ID)),
                    cursor.getString(cursor.getColumnIndex(ROOM_NAME)),
                    cursor.getString(cursor.getColumnIndex(PAYMENT_STATUS)))
    }

    fun getAllRooms() : List<Room> {
        val result : ArrayList<Room> = ArrayList<Room>()
        val selectAllQuery = "SELECT * FROM " + TABLE_ROOM
        Log.e(LOG, selectAllQuery)

        val db : SQLiteDatabase = this.writableDatabase
        val c : Cursor = db.rawQuery(selectAllQuery, null)

        if (c.moveToFirst()) {
            do {
                val room = Room(c.getLong(c.getColumnIndex(ID)),
                                c.getString(c.getColumnIndex(ROOM_NAME)),
                                c.getString(c.getColumnIndex(PAYMENT_STATUS)))
                result.add(room)
            } while (c.moveToNext())
        }
        return result
    }

    fun updateRoom(room: Room) : Int {
        val db : SQLiteDatabase = this.writableDatabase

        val values : ContentValues = ContentValues()
        values.put(FULL_NAME, room.roomName)
        values.put(PHONE_NUMER, room.paymentStatus)

        return db.update(TABLE_ROOM, values, ID + " = ?", arrayOf(room.roomId.toString()))
    }

    fun deleteRoom(roomId : Long) {
        val db : SQLiteDatabase = this.writableDatabase
        db.delete(TABLE_ROOM, ID + " = ?", arrayOf(roomId.toString()))
    }

    companion object {

        private val LOG: String = "DatabaseHelper"
        private val DB_VERSION = 1
        private val DB_NAME = "MyRooms"
        private val isInitializeDB = false

        //TABLE NAME
        private val TABLE_HOLDER = "holder"
        private val TABLE_ROOM = "room"

        //DB TYPE
        private val TEXT = "TEXT"
        private val INTEGER = "INTEGER"

        //Common column name
        private val ID = "id"
        private val CREATED_AT = "created_at"

        //Holder TABLE - column name
        private val FULL_NAME = "full_name"
        private val PHONE_NUMER = "phone_number"
        private val ID_CARD = "id_card"
        private val BIRTHDAY = "birthday"
        private val ADDRESS = "address"
        private val PROFILE_IMAGE = "profile_image"
        private val ROOM_ID = "room_id"

        //Room TABLE - column name
        private val ROOM_NAME = "room_name"
        private val PAYMENT_STATUS = "payment_status"
    }

}