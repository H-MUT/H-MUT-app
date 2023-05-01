package com.example.healthmyusualtime.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import com.example.healthmyusualtime.Login
import com.example.healthmyusualtime.databinding.ActivityUserIntersetBinding
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class UserInterset : AppCompatActivity() {
    lateinit var binding: ActivityUserIntersetBinding
    val MAX_SELECTION = 3
    var interList = ArrayList<String>()
    var buttonList = ArrayList<Button>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserIntersetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setViews()

        binding.previous.setOnClickListener(){
            finish()
        }

        binding.UserInfoComplete.setOnClickListener(){
            val intent = Intent(this, WelcomeUser::class.java)
            startActivity(intent)
//            postUser()
        }

    }

    fun setViews(){
        makebtn()
    }
    fun makebtn(){
        val tmpList = ArrayList<String>()
        tmpList.add("헬스")
        tmpList.add("필라테스")
        tmpList.add("1")
        tmpList.add("2")
        tmpList.add("3")
        // 여기에 서버 통신으로 리스트를 받아올 예정
        val linearLayout = binding.buttonContainer // 버튼들을 추가할 LinearLayout

        for (i in 0 until tmpList.size) {
            if (i % 3 == 0) { // 3개씩 배치하기 위해 나머지가 0일 때마다 새로운 LinearLayout 추가
                val newLinearLayout = LinearLayout(this)
                newLinearLayout.orientation = LinearLayout.HORIZONTAL
                newLinearLayout.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                linearLayout.addView(newLinearLayout)
            }

            // 버튼 추가
            val newButton = Button(this)
            buttonList.add(newButton)
            newButton.text = tmpList[i]
            newButton.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
            ) // 3개의 버튼을 동일한 너비로 배치하기 위해 weight를 1로 설정
            val currentLinearLayout = linearLayout.getChildAt(linearLayout.childCount - 1) as LinearLayout
            currentLinearLayout.addView(newButton)
            newButton.setOnClickListener {
                selectButton(newButton)
            }
        }
    }

    fun selectButton(button: Button) {
        button.isSelected = !button.isSelected
        if (button.isSelected) {
            // 선택된 버튼 개수가 MAX_SELECTION을 넘어가면 선택을 해제
            if (getSelectedCount() > MAX_SELECTION) {
                button.isSelected = false
                interList.remove(button.text.toString())
                return
            }
            // 선택된 버튼의 값을 interlist에 추가
            interList.add(button.text.toString())
        } else {
            // 선택 취소된 버튼의 값을 interlist에서 제거
            interList.remove(button.text.toString())
        }
    }

    // 선택된 버튼 개수 반환 함수
    fun getSelectedCount(): Int {
        var count = 0
        for (button in buttonList) {
            if (button.isSelected) count++
        }
        return count
    }

    fun postUser(information: UserInfo) {
        var username = intent.getStringExtra("name")!!
        val gson = GsonBuilder().create()
        val str = gson.toJson(information)
        val okHttpClient = OkHttpClient()
        val requestBody = str.toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .method("POST", requestBody)
            .url("")
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                CoroutineScope(Dispatchers.Main).launch{
                    Toast.makeText(applicationContext,"실패", Toast.LENGTH_SHORT).show()
                }
                Log.e("fail",e.message.toString())
                Log.e("fail",e.toString())
            }
            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful) {
                    HmutSharedPreferences.setUserInterest(this@UserInterset, interList)
                    HmutSharedPreferences.setUserName(this@UserInterset, username)
                    Log.i("Success", response.message)
                    Log.i("Success", response.toString())
                    finish()
                }
                else{
                    Log.e("connection error",response.body.toString())
                }
            }
        })
    }

    override fun onRestart() {
        super.onRestart()
        finish()
    }
}