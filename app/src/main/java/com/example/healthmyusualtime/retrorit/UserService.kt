package com.example.healthmyusualtime.retrorit

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("auth/join")
    fun postUser(
        @Body jsonparams: PostUser
    ): Call<data>
    companion object {
        private const val BASE_URL = "http://3.36.163.92/api/" // 주소

        fun create(): UserService {

            val gson : Gson =   GsonBuilder().setLenient().create();

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(UserService::class.java)
        }
    }
}