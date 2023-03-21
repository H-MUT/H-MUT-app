package com.example.healthmyusualtime.group

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.healthmyusualtime.R

class GroupMain : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_main)
        setGroupView()
    }
    fun setGroupView(){
//        feedRecyclerView = binding.RVHomeFeed
//        val groupName = intent.getIntExtra("groupName",0)
//        val gson = GsonBuilder().create()
//        val userId = Manager.information.userId
//        val okHttpClient = OkHttpClient()
//        val request = Request.Builder()
//            .method("GET", null)
//            .url("")
//            .build()
//        okHttpClient.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                Log.e("fail to get feed",e.message.toString())
//                Log.e("fail to get feed",e.toString())
//            }
//            override fun onResponse(call: Call, response: Response) {
//                if(response.isSuccessful) {
//
//                    Log.d("Success to feed",response.message)
//                    Log.d("Success to feed",response.toString())
//                    val tmpStr = response.body.string()
//                    val list = ArrayList<DataHomeFeed>()
//                    val feedList = gson.fromJson<Datafeed>(tmpStr, Datafeed::class.java)
//                    list.add(feedList)
//                    val feedRVAdapter = GroupFeedAdapter(this@ClickFeed,list,true)
//                    CoroutineScope(Dispatchers.Main).launch{
//                        feedRecyclerView.adapter = feedRVAdapter
//                    }
//                }
//                else{
//                    Log.e("fail to connection",response.message)
//                    Log.e("fail to connection",response.toString())
//
//                }
//            }
//        })
    }

}