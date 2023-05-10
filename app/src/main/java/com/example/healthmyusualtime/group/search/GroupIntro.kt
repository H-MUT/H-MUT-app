package com.example.healthmyusualtime.group.search

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.healthmyusualtime.databinding.ActivityGroupIntroBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class GroupIntro : AppCompatActivity() {
    lateinit var binding : ActivityGroupIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupIntroBinding.inflate(layoutInflater)

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
    }
}