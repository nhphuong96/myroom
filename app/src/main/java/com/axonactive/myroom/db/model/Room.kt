package com.axonactive.myroom.db.model

/**
 * Created by Phuong Nguyen on 6/14/2018.
 */
class Room {
    var roomId : Long? = null
    var roomName: String
    var paymentStatus : String


    constructor(roomId: Long?, name : String, pStatus : String) {
        this.roomId = roomId
        this.roomName = name
        this.paymentStatus = pStatus
    }
}