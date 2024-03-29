package com.example.healthmyusualtime.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import com.example.healthmyusualtime.Login
import com.example.healthmyusualtime.R
import com.example.healthmyusualtime.R.drawable
import com.example.healthmyusualtime.databinding.ActivityUserIntersetBinding
import com.example.healthmyusualtime.retrorit.PostUser
import com.example.healthmyusualtime.retrorit.UserService
import com.example.healthmyusualtime.retrorit.data
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kakao.sdk.user.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class UserInterset : AppCompatActivity() {
    lateinit var binding: ActivityUserIntersetBinding
    val MAX_SELECTION = 3
    var interList = ArrayList<String>()
    var buttonList = ArrayList<Button>()
    lateinit var api : UserService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        api = UserService.create()
        binding = ActivityUserIntersetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setViews()

        binding.previous.setOnClickListener(){
            finish()
        }

        binding.UserInfoComplete.setOnClickListener(){
            Log.i("test", "test")
            val image = HmutSharedPreferences.getUserImageUrl(this)
            var username = intent.getStringExtra("name")!!
            val tag = ArrayList<String>()
            tag.add("YOGA")
            tag.add("CYCLE")
            var information = UserInfo("esz12@naver.com",image,username,tag)
            postUser(information)
            val intent = Intent(this, WelcomeUser::class.java)
            startActivity(intent)

        }

    }

    fun setViews(){
        makebtn()
    }
    fun makebtn(){
        val tmpList = ArrayList<String>()
        tmpList.add("헬스")
        tmpList.add("필라테스")
        tmpList.add("클라이밍")
        tmpList.add("요가")
        tmpList.add("런닝")
        tmpList.add("수영")
        tmpList.add("싸이클")
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
            newButton.setBackgroundResource(R.drawable.round_button)
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
            button.setBackgroundResource(R.drawable.select_button)

        } else {
            // 선택 취소된 버튼의 값을 interlist에서 제거
            interList.remove(button.text.toString())
            button.setBackgroundResource(R.drawable.round_button)
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

//    fun postUser(info : UserInfo){
//        val retrofit = UserService.create().postUser(PostUser(info.email,info.profileImage,info.name,info.tagValues))
//        retrofit.enqueue(object : Callback<data> {
//            override fun onResponse(call: Call<data>, response: Response<data>) {
//                if(response.isSuccessful() && (response.body() != null)){
//                    Log.i("test", response.body().toString())
////                    Thread{
////                        var a = response.body()
////                        Log.i("test",a)
////                    }.start()
//                }
//                else{
//                    Log.e("connection error",response.body().toString())
//                }
//
//            }
//
//            override fun onFailure(call: Call<data>, t: Throwable) {
//                Log.e("fail",t.message.toString())
//                Log.e("fail",t.toString())
//            }
//        })
//   }

    fun postUser(information: UserInfo) {
        var username = intent.getStringExtra("name")!!
        val gson = GsonBuilder().create()
        val str = gson.toJson(information)
        val okHttpClient = OkHttpClient()
        val requestBody = str.toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .method("POST", requestBody)
            .url("http://3.36.163.92/api/auth/join")
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
//                    HmutSharedPreferences.setUserName(this@UserInterset, username)
                    Log.i("Success", response.message)
                    Log.i("Success", response.toString())
//                    HmutSharedPreferences.setUserName(this@UserInterset,"김원진")
                    Thread{
                        var str = response.body?.string()
                        Log.i("test",str!!)
//                        val a = gson.fromJson<data>(str,data::class.java)
                        var tmp = JSONObject(response.body.string())
                        println(tmp.getString("data"))

                    }.start()



                    finish()
                }
                else{
                    Log.e("connection error",response.body.string())
                }
            }
        })
    }

    override fun onRestart() {
        super.onRestart()
        finish()
    }
}