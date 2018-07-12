package com.axonactive.myroom.db.model

import java.util.*

/**
 * Created by Phuong Nguyen on 6/13/2018.
 */
class Holder {
    var holderId : Long? = null
    var fullName : String
    var phoneNumber : String
    var idCard : String?
    var birthDate : Date?
    var address : String?
    var profileImage : String?
    var roomId : Long? = null
    var createdAt: String?

    constructor(holderId: Long?, fName : String, pNumber : String, idCard : String?, bDate: Date?, add : String?, pImage : String?, createdAt : String?, roomId : Long) {
        this.holderId = holderId
        this.fullName = fName
        this.phoneNumber = pNumber
        this.idCard = idCard
        this.birthDate = bDate
        this.address = add
        this.profileImage = pImage
        this.createdAt = createdAt
        this.roomId = roomId
    }
}