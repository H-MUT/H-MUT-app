package com.example.healthmyusualtime.group.feed

import com.example.healthmyusualtime.Information
import com.example.healthmyusualtime.login.Manager

class Datafeed (
    val writer: Information = Information("","",Manager.information.name,null,null),
    val contentImg : ByteArray?,
    var likeNum: Int = 0,
    val content : String?,
    val groupId : String?,
    val feedId : Int?,
    var isLiked: Boolean?,
    var saveUrl : String?
) {
}