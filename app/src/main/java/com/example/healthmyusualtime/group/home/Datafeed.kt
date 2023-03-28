package com.example.healthmyusualtime.group.home

import com.example.healthmyusualtime.login.Manager
import com.example.healthmyusualtime.login.UserInfo

class Datafeed (
    val writer: UserInfo = UserInfo(Manager.userInfo.name,null,null,null),
    val contentImg : ByteArray?,
    var likeNum: Int = 0,
    val content : String?,
    val groupId : String?,
    val feedId : Int?,
    var isLiked: Boolean?,
    var saveUrl : String?
) {
}