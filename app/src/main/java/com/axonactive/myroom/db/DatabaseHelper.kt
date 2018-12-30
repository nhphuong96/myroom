package com.axonactive.myroom.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.axonactive.myroom.db.model.*
import com.axonactive.myroom.db.model.Unit
import com.axonactive.myroom.utils.DateUtils
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Phuong Nguyen on 6/13/2018.
 */
class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DatabaseHelper.DB_NAME, null, DatabaseHelper.DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        createTable(db, TABLE_HOLDER, listOf(FULL_NAME, PHONE_NUMER, BIRTHDAY, ID_CARD, ADDRESS, PROFILE_IMAGE, ROOM_ID, IS_OWNER)
                                    , listOf(TEXT, TEXT, TEXT, TEXT, TEXT, TEXT, INTEGER, INTEGER)
                                    , true)
        createTable(db, TABLE_ROOM, listOf(ROOM_NAME, PAYMENT_STATUS),
                                    listOf(TEXT, TEXT),
                                    true)
        createTable(db, TABLE_ATTRIBUTE, listOf(ATTRIBUTE_NAME, ATTRIBUTE_ICON)
                                            , listOf(TEXT, TEXT), true)
        createTable(db, TABLE_UNIT, listOf(UNIT_NAME)
                                    , listOf(TEXT), true)

        createTable(db, TABLE_ROOM_ATTRIBUTE, listOf("room_id", "attribute_id", "unit_id" , "value")
                                            , listOf(INTEGER, INTEGER, INTEGER, TEXT), true)

        //Insert default value
        insertUnitTable(db)
        insertAttributeTable(db)
    }

    private fun insertUnitTable(db: SQLiteDatabase) {
        val values = ArrayList<ContentValues>()
        var contentValues = ContentValues()
        contentValues.put(UNIT_NAME, "kW")
        var contentValues1 = ContentValues()
        contentValues1.put(UNIT_NAME, "person")
        var contentValues2 = ContentValues()
        contentValues2.put(UNIT_NAME, "month")
        var contentValues3 = ContentValues()
        contentValues3.put(UNIT_NAME, "m\u00B3")
        values.add(contentValues)
        values.add(contentValues1)
        values.add(contentValues2)
        values.add(contentValues3)
        for (value in values) {
            db.insert(TABLE_UNIT, null, value)
        }
    }

    private fun insertAttributeTable(db: SQLiteDatabase) {
        val values = ArrayList<ContentValues>()
        var contentValues = ContentValues()
        contentValues.put(ATTRIBUTE_NAME, "Electricity")
        contentValues.put(ATTRIBUTE_ICON, "ic_electric_red")
        var contentValues1 = ContentValues()
        contentValues1.put(ATTRIBUTE_NAME, "Water")
        contentValues1.put(ATTRIBUTE_ICON, "ic_water_drop_red")
        var contentValues2 = ContentValues()
        contentValues2.put(ATTRIBUTE_NAME, "Internet")
        contentValues2.put(ATTRIBUTE_ICON, "ic_wifi_red")
        var contentValues3 = ContentValues()
        contentValues3.put(ATTRIBUTE_NAME, "Cab")
        contentValues3.put(ATTRIBUTE_ICON, "ic_television_red")
        var contentValues4 = ContentValues()
        contentValues4.put(ATTRIBUTE_NAME, "Parking")
        contentValues4.put(ATTRIBUTE_ICON, "ic_parked_car_red")
        values.add(contentValues)
        values.add(contentValues1)
        values.add(contentValues2)
        values.add(contentValues3)
        values.add(contentValues4)
        for (value in values) {
            db.insert(TABLE_ATTRIBUTE, null, value)
        }
    }

    private fun createTable(db: SQLiteDatabase, tableName: String, headers : List<String>, headerTypes : List<String>, isIncludeIdCol: Boolean)
    {
        val builder = StringBuilder("CREATE TABLE ")
        if (!isInitializeDB) {
            builder.append(" IF NOT EXISTS ")
        }
        if (isIncludeIdCol) {
            builder.append(tableName).append("(").append(DatabaseHelper.ID).append(" INTEGER PRIMARY KEY,")
        }
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
        values.put(IS_OWNER, holder.isOwner)
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
                    cursor.getLong(cursor.getColumnIndex(ROOM_ID)),
                    cursor.getInt(cursor.getColumnIndex(IS_OWNER)))
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
                        c.getLong(c.getColumnIndex(ROOM_ID)),
                        c.getInt(c.getColumnIndex(IS_OWNER)))
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
                        c.getLong(c.getColumnIndex(ROOM_ID)),
                        c.getInt(c.getColumnIndex(IS_OWNER)))
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
        values.put(IS_OWNER, holder.isOwner)

        return db.update(TABLE_HOLDER, values, ID + " = ?", arrayOf(holder.holderId.toString()))
    }

    fun deleteHolder(holderId : Long) {
        val db : SQLiteDatabase = this.writableDatabase
        db.delete(TABLE_HOLDER, ID + " = ?", arrayOf(holderId.toString()))
    }

    fun deleteHoldersByRoomId(roomId: Long) : Int{
        val db : SQLiteDatabase = this.writableDatabase
        return db.delete(TABLE_HOLDER, ROOM_ID + " = ?", arrayOf(roomId.toString()))
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

    fun deleteRoom(roomId : Long) : Int {
        val db : SQLiteDatabase = this.writableDatabase
        return db.delete(TABLE_ROOM, ID + " = ?", arrayOf(roomId.toString()))
    }

    /*----------------------------------- UNIT - CRUD --------------------------------------*/
    fun getAllUnits() : List<Unit> {
        val result : ArrayList<Unit> = ArrayList<Unit>()
        val selectAllQuery = "SELECT * FROM " + TABLE_UNIT
        Log.e(LOG, selectAllQuery)

        val db : SQLiteDatabase = this.writableDatabase
        val c : Cursor = db.rawQuery(selectAllQuery, null)

        if (c.moveToFirst()) {
            do {
                val unit = Unit(c.getLong(c.getColumnIndex(ID)),
                        c.getString(c.getColumnIndex(UNIT_NAME)))
                result.add(unit)
            } while (c.moveToNext())
        }
        return result
    }

    fun getUnitById(unitId : Long) : Unit? {
        val selectQuery = "SELECT * FROM " + TABLE_UNIT + " WHERE " + ID + " = " + unitId
        Log.e(LOG, selectQuery)

        val db : SQLiteDatabase = this.writableDatabase
        val c : Cursor = db.rawQuery(selectQuery, null)

        if (c.moveToFirst()) {
            return Unit(c.getLong(c.getColumnIndex(ID)),
                    c.getString(c.getColumnIndex(UNIT_NAME)))
        }
        return null
    }

    /*-----------------------------------  ATTRIBUTE - CRUD ------------------------------*/
    fun getAllAttributes() : List<Attribute> {
        val result : ArrayList<Attribute> = ArrayList<Attribute>()
        val selectAllQuery = "SELECT * FROM " + TABLE_ATTRIBUTE
        Log.e(LOG, selectAllQuery)

        val db : SQLiteDatabase = this.writableDatabase
        val c : Cursor = db.rawQuery(selectAllQuery, null)

        if (c.moveToFirst()) {
            do {
                val attr = Attribute(c.getLong(c.getColumnIndex(ID)),
                                        c.getString(c.getColumnIndex(ATTRIBUTE_NAME)),
                                        c.getString(c.getColumnIndex(ATTRIBUTE_ICON)))
                result.add(attr)
            } while (c.moveToNext())
        }
        return result
    }

    fun getAttributeById(id: Long) : Attribute? {
        val selectAllQuery = "SELECT * FROM " + TABLE_ATTRIBUTE + " WHERE ID = " + id
        Log.e(LOG, selectAllQuery)

        val db : SQLiteDatabase = this.writableDatabase
        val c : Cursor = db.rawQuery(selectAllQuery, null)

        if (c.moveToFirst()) {
            val attr = Attribute(c.getLong(c.getColumnIndex(ID)),
                    c.getString(c.getColumnIndex(ATTRIBUTE_NAME)),
                    c.getString(c.getColumnIndex(ATTRIBUTE_ICON)))
            return attr
        }
        return null
    }

    /*------------------------------------- ROOM ATTRIBUTE - CRUD ---------------------------*/
    fun createRoomAttribute(roomId: Long, attributeId: Long, value : String?, unitId : Long) : Long {
        val db : SQLiteDatabase = this.writableDatabase
        val values = ContentValues()
        values.put("room_id", roomId)
        values.put("attribute_id", attributeId)
        values.put("unit_id", unitId)
        if (value != null) {
            values.put("value", value)
        }
        values.put(CREATED_AT, DateUtils.toSimpleDateString(Date()))
        return db.insert(TABLE_ROOM_ATTRIBUTE, null, values)
    }

    fun getRoomAttribute(roomId: Long, attributeId: Long?) : RoomAttribute? {
        val db : SQLiteDatabase = this.writableDatabase

        val selectQuery = "SELECT * FROM " + TABLE_ROOM_ATTRIBUTE + " WHERE room_id = " + roomId + " AND attribute_id = " + attributeId

        Log.e(LOG, selectQuery)

        val c : Cursor = db.rawQuery(selectQuery, null)
        if (c.moveToFirst()) {
            val attr = RoomAttribute(this.getRoom(c.getLong(c.getColumnIndex("room_id"))),
                    this.getAttributeById(c.getLong(c.getColumnIndex("attribute_id"))),
                    this.getUnitById(c.getLong(c.getColumnIndex("unit_id"))),
                    c.getString(c.getColumnIndex("value")))
            return attr
        }
        return null
    }
    fun getRoomAttributeByRoomId(roomId: Long) : List<Attribute> {
        val db : SQLiteDatabase = this.writableDatabase
        var result = ArrayList<Attribute>()

        val selectQuery = "SELECT * FROM " + TABLE_ROOM_ATTRIBUTE + " WHERE room_id = " + roomId

        Log.e(LOG, selectQuery)

        val c : Cursor = db.rawQuery(selectQuery, null)
        if (c.moveToFirst()) {
            do {
                val attr = this.getAttributeById(c.getLong(c.getColumnIndex("attribute_id")))
                result.add(attr!!)
            } while (c.moveToNext())
        }
        return result
    }

    fun deleteRoomAttribute(roomId: Long, attributeId: Long?) : Int {
        val db : SQLiteDatabase = this.writableDatabase
        var whereClause = StringBuilder()
        var ids = ArrayList<String>()
        whereClause.append("room_id = ?")
        ids.add(roomId.toString())
        if (attributeId != null) {
            whereClause.append("attribute_id = ?")
            ids.add(attributeId.toString())
        }
        return db.delete(TABLE_ROOM_ATTRIBUTE, whereClause.toString(), ids.toTypedArray())
    }

    fun updateRoomAttribute(roomId: Long, attributeId: Long, newValue : String, newUnit: Long) : Int {
        val db : SQLiteDatabase = this.writableDatabase
        val values = ContentValues()
        values.put("unit_id", newUnit)
        values.put("value", newValue)

        var whereClause = StringBuilder()
        var ids = ArrayList<String>()
        whereClause.append("room_id = ?")
        ids.add(roomId.toString())
        if (attributeId != null) {
            whereClause.append(" and attribute_id = ?")
            ids.add(attributeId.toString())
        }

        return db.update(TABLE_ROOM_ATTRIBUTE, values ,whereClause.toString(), ids.toTypedArray())
    }

    companion object {

        private val LOG: String = "DatabaseHelper"
        private val DB_VERSION = 1
        private val DB_NAME = "MyRooms"
        private val isInitializeDB = false

        //TABLE NAME
        private val TABLE_HOLDER = "holder"
        private val TABLE_ROOM = "room"
        private val TABLE_ATTRIBUTE = "attribute"
        private val TABLE_UNIT = "unit"
        private val TABLE_ROOM_ATTRIBUTE = "room_attribute"

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
        private val IS_OWNER = "is_owner"

        //Room TABLE - column name
        private val ROOM_NAME = "room_name"
        private val PAYMENT_STATUS = "payment_status"

        //Room Attribute TABLE - column name
        private val ATTRIBUTE_NAME = "attribute_name"
        private val ATTRIBUTE_UNIT = "unit"
        private val ATTRIBUTE_ICON = "icon"

        //Unit TABLE - column name
        private val UNIT_NAME = "unit_name"
    }

}