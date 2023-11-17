package com.example.healthmyusualtime.group

import android.net.Uri


class DataGroup(val imageUrl : Uri?,
                val adminName : String?,
                val groupName : String?,
                val tags : String?,
                val groupFrequency : String?,
                val introduceMessage : String?,
                val description : String?,
                val groupMember : Int?,
                val groupId : Int?) {
}