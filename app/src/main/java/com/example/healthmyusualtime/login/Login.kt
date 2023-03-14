package com.example.healthmyusualtime

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.healthmyusualtime.databinding.ActivityLoginBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient

class Login : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        KakaoSdk.init(this, "kakao14c95fc7ebfefa125b11688f97587e34")

        val btn = findViewById<Button>(R.id.loginBtn)
        val kakaobtn = binding.kakaoSignUp

//        if(UserInformation.getUserId(this).isNullOrBlank()     // 저장된 로그인 값이 없을 때
//            || UserInformation.getUserPw(this).isNullOrBlank()) {
//            Login()
//        }
//        else {                  // 저장된 로그인 값이 있을 때 --> loginActivity 종료
//            finish()
//        }
        //서버 통신하면 살릴거
        btn.setOnClickListener(){
            finish()
        }
        //서버 통신하면 지울거 로그인 버튼 누르면 무조건 넘어가짐~
        binding.signUp.setOnClickListener() {
            val sign_up = Intent(this, Sign_Up::class.java)
            startActivity(sign_up)
        }

        kakaobtn.setOnClickListener(){

            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오계정으로 로그인 실패", error)
                } else if (token != null) {
                    Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

// 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        Log.e(TAG, "카카오톡으로 로그인 실패", error)

                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }

                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                    } else if (token != null) {
                        Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }


    }
    fun Login() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        binding.loginBtn.setOnClickListener(){
            var userID = binding.textID.text.toString()
            var userPW = binding.textPW.text.toString()
            val loginInfo = LoginInfo(userID, userPW)
            postLogin(loginInfo)
        }
    }
//    fun kakaologin(context: Context){
//        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
//            if (error != null) {
//                Log.e(TAG, "카카오계정으로 로그인 실패", error)
//            } else if (token != null) {
//                Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
//                val intent = Intent(applicationContext, MainActivity::class.java)
//                startActivity(intent)
//                finish()
//            }
//        }
//
//// 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
//        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
//            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
//                if (error != null) {
//                    Log.e(TAG, "카카오톡으로 로그인 실패", error)
//
//                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
//                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
//                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
//                        return@loginWithKakaoTalk
//                    }
//
//                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
//                    UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
//                } else if (token != null) {
//                    Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
//                    val intent = Intent(applicationContext, MainActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                }
//            }
//        } else {
//            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
//        }
//    }
    fun postLogin(loginInfo: LoginInfo) {
//        val gson = GsonBuilder().create()
//        val str = gson.toJson(loginInfo)
//        val okHttpClient = OkHttpClient()
//        val requestBody = str.toRequestBody("application/json".toMediaTypeOrNull())
//        val request = Request.Builder()
//            .method("POST", requestBody)
//            .url("주소")
//            .build()
//        okHttpClient.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                CoroutineScope(Dispatchers.Main).launch{
//                    Toast.makeText(applicationContext,"로그인 실패",Toast.LENGTH_SHORT).show()
//                }
//                Log.e("fail to login",e.message.toString())
//                Log.e("fail to login",e.toString())
//            }
//            override fun onResponse(call: Call, response: Response) {
//                if(response.isSuccessful) {
//                    val tmpname = response.body.string()
//                    val username = gson.fromJson(tmpname, String::class.java)
//                    UserInformation.setUserId(applicationContext, textID.text.toString())
//                    UserInformation.setUserPw(applicationContext, textPW.text.toString())
//                    UserInformation.setUserName(applicationContext, username)
//
//                    Log.i("Success", response.message)
//                    Log.i("Success", response.toString())
//                    val intent = Intent(applicationContext, MainActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                }
//                else{
//                    Log.e("connection error",response.body.toString())
//                }
//            }
//        })
        //서버 생기면 살릴거
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


}
data class LoginInfo(val userId: String, var password: String) {
}