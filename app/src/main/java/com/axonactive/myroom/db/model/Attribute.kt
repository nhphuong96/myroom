package com.axonactive.myroom.db.model

/**
 * Created by Phuong Nguyen on 7/22/2018.
 */
class Attribute {
    var attributeId : Long? = null
    var name : String
    var icon : String

    constructor(id: Long?, name : String, icon: String) {
        this.attributeId = id
        this.name = name
        this.icon = icon
    }
}