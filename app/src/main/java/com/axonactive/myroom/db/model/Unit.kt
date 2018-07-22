package com.axonactive.myroom.db.model

/**
 * Created by Phuong Nguyen on 7/22/2018.
 */
class Unit {
    var unitId : Long? = null
    var unitName: String

    constructor(id: Long, name: String) {
        this.unitId = id
        this.unitName = name
    }
}