package com.example.healthmyusualtime.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.healthmyusualtime.MainActivity
import com.example.healthmyusualtime.R
import com.example.healthmyusualtime.databinding.ActivityWelcomeUserBinding

class WelcomeUser : AppCompatActivity() {
    lateinit var binding : ActivityWelcomeUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeUserBinding.inflate(layoutInflater)

        binding.start.setOnClickListener(){
            finish()
        }
        binding.welcome.text = "${HmutSharedPreferences.getUserName(this)}님, \nHMUT에\n오신것을 환영합니다."
        setContentView(binding.root)
    }
}