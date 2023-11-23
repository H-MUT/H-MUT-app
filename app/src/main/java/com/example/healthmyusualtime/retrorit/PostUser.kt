package com.example.healthmyusualtime.retrorit

import com.google.gson.annotations.SerializedName

data class PostUser(

    @SerializedName("email")
    val email : String?,

    @SerializedName("profileImage")
    val profileImage: String?,

    @SerializedName("name")
    val name : String?,

    @SerializedName("tagValues")
    val tagValues : ArrayList<String>?,

)

data class data(
    @SerializedName("data.accessToken")
    val accessToken : String? = null,

    @SerializedName("data.refreshToken")
    val refreshToken : String? = null,

    @SerializedName("data.userId")
    val userId : String? = null
)

//  email, profileImage, name, tagValues ListArray<String>