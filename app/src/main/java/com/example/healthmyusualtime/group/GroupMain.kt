package com.example.healthmyusualtime.group

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.healthmyusualtime.R
import com.example.healthmyusualtime.databinding.ActivityGroupMainBinding

class GroupMain : AppCompatActivity() {
    lateinit var binding: ActivityGroupMainBinding

    val fl: ConstraintLayout by lazy{
        findViewById(R.id.GMain)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val groupname = intent.getStringExtra("groupName")
        binding = ActivityGroupMainBinding.inflate(layoutInflater)
        binding.GName.text = groupname
        setContentView(binding.root)

        val bottomNavigationView = binding.GBottomBar

        supportFragmentManager.beginTransaction().add(fl.id, GroupHomeFragment()).commit()

        bottomNavigationView.setOnItemSelectedListener { item ->
            changeFragment(
                when (item.itemId) {
                    R.id.group_home -> {
                        bottomNavigationView.itemIconTintList = ContextCompat.getColorStateList(this, R.color.color_home)
                        bottomNavigationView.itemTextColor = ContextCompat.getColorStateList(this, R.color.color_home)
                        GroupHomeFragment()
                    }
                    R.id.group_chat -> {
                        bottomNavigationView.itemIconTintList = ContextCompat.getColorStateList(this, R.color.color_search_group)
                        bottomNavigationView.itemTextColor = ContextCompat.getColorStateList(this, R.color.color_search_group)
                        GroupChatFragment()
                    }

                    else -> {
                        bottomNavigationView.itemIconTintList = ContextCompat.getColorStateList(this, R.color.color_user)
                        bottomNavigationView.itemTextColor = ContextCompat.getColorStateList(this, R.color.color_user)
                        GroupSettingFragment()
                    }
                }
            )
            true
        }
        bottomNavigationView.selectedItemId = R.id.group_home



    }

    fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.GMain, fragment)
            .commit()
    }
    fun setGroupView(){
//        feedRecyclerView = binding.RVHomeFeed
//        val GName = intent.getIntExtra("groupName",0)
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