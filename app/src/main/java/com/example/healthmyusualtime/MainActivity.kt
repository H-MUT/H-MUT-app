package com.example.healthmyusualtime

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.healthmyusualtime.home.HomeFragment
import com.example.healthmyusualtime.login.HmutSharedPreferences
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility

class MainActivity : AppCompatActivity() {

    private val fl: ConstraintLayout by lazy{
        findViewById(R.id.main_layout)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        HmutSharedPreferences.Logout_User(this)
        if(true){       //로그인 안돼있으면
            val loginIntent = Intent(this, Login::class.java)
            startActivity(loginIntent)
        }
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_bar)

        supportFragmentManager.beginTransaction().add(fl.id, HomeFragment()).commit()

        bottomNavigationView.setOnItemSelectedListener { item ->
            changeFragment(
                when (item.itemId) {
                    R.id.menu_home -> {
                        bottomNavigationView.itemIconTintList = ContextCompat.getColorStateList(this, R.color.color_home)
                        bottomNavigationView.itemTextColor = ContextCompat.getColorStateList(this, R.color.color_home)
                        HomeFragment()
                    }
                    R.id.menu_group_search -> {
                        bottomNavigationView.itemIconTintList = ContextCompat.getColorStateList(this, R.color.color_search_group)
                        bottomNavigationView.itemTextColor = ContextCompat.getColorStateList(this, R.color.color_search_group)
                        SearchGroupFragment()
                    }

                    else -> {
                        bottomNavigationView.itemIconTintList = ContextCompat.getColorStateList(this, R.color.color_user)
                        bottomNavigationView.itemTextColor = ContextCompat.getColorStateList(this, R.color.color_user)
                        UserFragment()
                    }
                }
            )
            true
        }
        bottomNavigationView.selectedItemId = R.id.menu_home

        val keyHash = Utility.getKeyHash(this)
        Log.d("Hash", keyHash)

    }
    fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_layout, fragment)
            .commit()
    }


}