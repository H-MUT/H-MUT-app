package com.example.healthmyusualtime

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.healthmyusualtime.databinding.ActivityLoginBinding
import com.example.healthmyusualtime.group.DataGroup
import com.example.healthmyusualtime.login.HmutSharedPreferences
import com.example.healthmyusualtime.login.UserInformation
import com.google.gson.GsonBuilder
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.Constants
import com.kakao.sdk.user.UserApiClient
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

class Login : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val btn = binding.loginBtn
        val kakaobtn = binding.kakaoSignUp

        btn.setOnClickListener(){
            finish()
        }
        // 서버 통신하면 지워야 됨 현재는 로그인 버튼 클릭시 로그인 됏다고 가정

        kakaobtn.setOnClickListener(){
            kakaologin(this)
        }


    }

    fun kakaologin(context: Context){
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
                if(HmutSharedPreferences.getUserName(this).isNullOrBlank()){ // 저장된 로그인 값이 없을 때
                    val intent = Intent(applicationContext, UserInformation::class.java)
                    startActivity(intent)
                    if(HmutSharedPreferences.getUserName(this).isNotBlank())
                        finish()
                }
                else {
                    finish()
                }
            }
        }

// 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                } else if (token != null) {
                    Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                    // 여기서 이제 accessToken을 서버로 전송 하면서 전송이 되었을때, 넘어가기
                    if(HmutSharedPreferences.getUserName(this).isNullOrBlank()){
                        // 저장된 로그인 값이 없을 때
                        getAccessToken(token.accessToken)
                        val intent = Intent(applicationContext, UserInformation::class.java)
                        startActivity(intent)
                        if(HmutSharedPreferences.getUserName(this).isNotBlank())
                            finish()
                    }
                    else {
                        finish()
                    }
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }

    var backPressedTime : Long = 0
    override fun onBackPressed() {              // 뒤로가기 버튼
        if (System.currentTimeMillis()-backPressedTime <= 2000) {
            finishAffinity()
        }
        else {
            backPressedTime = System.currentTimeMillis()
            Toast.makeText(applicationContext, "뒤로가기를 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
    }
    fun getAccessToken(token : String){
        val gson = GsonBuilder().create()
        val str = gson.toJson(token)
        val okHttpClient = OkHttpClient()
        val requestBody = str.toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .method("POST", requestBody)
            .url("http://3.36.163.92/api/auth/join")
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                CoroutineScope(Dispatchers.Main).launch{
                    Toast.makeText(applicationContext,"Fail to get access token",Toast.LENGTH_SHORT).show()
                }
                Log.e("fail to get access token",e.message.toString())
                Log.e("fail to get access token",e.toString())
            }
            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful) {
                    Log.d("Success to get access token",response.message)
                    Log.d("Success to get access token",response.toString())
                    CoroutineScope(Dispatchers.Main).launch{
                        Toast.makeText(applicationContext,"Success to get access token",Toast.LENGTH_SHORT).show()
                    }
                    finish()
                }
                else{
                    Log.e("fail to add connection",response.message)
                    Log.e("fail to add connection",response.toString())
                    CoroutineScope(Dispatchers.Main).launch{
                        Toast.makeText(applicationContext,"Fail to connection",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}