package com.example.healthmyusualtime.group.search

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.healthmyusualtime.databinding.ActivityGroupIntroBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

class GroupIntro : AppCompatActivity() {
    lateinit var binding : ActivityGroupIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupIntroBinding.inflate(layoutInflater)

        var groupid = intent.getIntExtra("groupid",0)

        binding.groupname.text = intent.getStringExtra("name")
        setContentView(binding.root)
        binding.day.setUsePercentValues(true)
        binding.week.setUsePercentValues(true)
        val dayEntry = ArrayList<PieEntry>()
        dayEntry.add(PieEntry(700f,"달성"))
        dayEntry.add(PieEntry(200f,"미달성"))
        val weekEntry = ArrayList<PieEntry>()
        weekEntry.add(PieEntry(500f,"달성"))
        weekEntry.add(PieEntry(800f,"미달성"))

        val pieDayDateSet = PieDataSet(dayEntry,"")
        pieDayDateSet.apply{
            valueTextColor = Color.BLACK
            valueTextSize = 15f
        }
        val pieWeekDataSet = PieDataSet(weekEntry,"")
        pieWeekDataSet.apply{
            valueTextColor = Color.BLACK
            valueTextSize = 15f
        }
        val piedayData = PieData(pieDayDateSet)
        binding.day.apply{
            data = piedayData
            description.isEnabled = false
            isRotationEnabled = false
            centerText = "일일 달성률"
        }
        val pieweekData = PieData(pieWeekDataSet)
        binding.week.apply{
            data = pieweekData
            description.isEnabled = false
            isRotationEnabled = false
            centerText = "주간 달성률"
        }
        binding.joinBtn.setOnClickListener(){
            joinGroup(groupid)
        }
    }

    fun joinGroup(id : Int){
        val gson = GsonBuilder().create()
        val str = gson.toJson(id)
        val okHttpClient = OkHttpClient()
        val requestBody = str.toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .method("POST", requestBody)
            .url("http://3.36.163.92/api/groups/${id}/users")
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                CoroutineScope(Dispatchers.Main).launch{
                    Toast.makeText(applicationContext,"연결 실패", Toast.LENGTH_SHORT).show()
                }
                Log.e("fail to join Group",e.message.toString())
                Log.e("fail to join Group",e.toString())
            }
            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful) {
                    Log.i("Success to join Group", response.message)
                    Log.i("Success to join Group", response.toString())
                }
                else{
                    Log.e("connection error",response.body.toString())
                }
            }
        })
    }
}