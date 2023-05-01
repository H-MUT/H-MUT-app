package com.example.healthmyusualtime.group.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.healthmyusualtime.SearchGroupFragment
import com.example.healthmyusualtime.group.DataGroup
import com.example.healthmyusualtime.home.HomeGroupAdapter
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class TagViewModel : ViewModel() {

    lateinit var groupRecyclerView : RecyclerView

    fun loadGroup(tag : String) : MutableList<DataGroup>{
        val list = ArrayList<DataGroup>()
//        val gson = GsonBuilder().create()
//        val str = gson.toJson(DataGroup(null,null,null,tag,null,null))
//        val okHttpClient = OkHttpClient()
//        val requestBody = str.toRequestBody("application/json".toMediaTypeOrNull())
//        val request = Request.Builder()
//            .method("GET", requestBody)
//            .url("")
//            .build()
//        okHttpClient.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                Log.e("fail to revise feed",e.message.toString())
//                Log.e("fail to revise feed",e.toString())
//            }
//            override fun onResponse(call: Call, response: Response) {
//                if(response.isSuccessful) {
//                    Log.d("Revise to feed",response.message)
//                    Log.d("Revise to feed",response.toString())
//
//                    val tmpStr = response.body.string()
//                    val groupList = gson.fromJson<DataGroup>(tmpStr, Array<DataGroup>::class.java)
//                    list.add(groupList)
//                }
//                else{
//                    Log.e("fail to connection",response.message)
//                    Log.e("fail to connection",response.toString())
//
//                }
//            }
//        })
        return list
    }

}