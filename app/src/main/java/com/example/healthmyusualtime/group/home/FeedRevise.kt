package com.example.healthmyusualtime.group.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.healthmyusualtime.databinding.ActivityFeedReviseBinding
import com.example.healthmyusualtime.login.Manager
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class FeedRevise : AppCompatActivity() {
    lateinit var binding : ActivityFeedReviseBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedReviseBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val tmp = intent.getStringExtra("content")!!
        binding.reviseContent.setText(tmp.toCharArray(),0,tmp.length)
        binding.cancelBtn.setOnClickListener(){
            finish()
        }
        binding.reviseBtn.setOnClickListener(){
            val text = binding.reviseContent.text
            val feedId = intent.getIntExtra("feedid",0)
            val gson = GsonBuilder().create()
            val str = gson.toJson(Datafeed(Manager.information,null,0,text.toString(),null,null,null, null))
            val okHttpClient = OkHttpClient()
            val requestBody = str.toRequestBody("application/json".toMediaTypeOrNull())
            val request = Request.Builder()
                .method("PUT", requestBody)
                .url("")
                .build()
            okHttpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("fail to revise feed",e.message.toString())
                    Log.e("fail to revise feed",e.toString())
                }
                override fun onResponse(call: Call, response: Response) {
                    if(response.isSuccessful) {
                        Log.d("Revise to feed",response.message)
                        Log.d("Revise to feed",response.toString())
                        finish()

                    }
                    else{
                        Log.e("fail to connection",response.message)
                        Log.e("fail to connection",response.toString())

                    }
                }
            })
        }

    }
}