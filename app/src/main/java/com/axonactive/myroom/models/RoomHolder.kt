package com.axonactive.myroom.models

import java.util.*

/**
 * Created by Phuong Nguyen on 5/16/2018.
 */
data class RoomHolder(var holderId : Long?,
                        var fullName: String,
                      var phoneNumber: String,
                      var imageName : String?,
                      var birthday : Date?,
                      var idCard : String?,
                      var isOwner : Int?)
{

}