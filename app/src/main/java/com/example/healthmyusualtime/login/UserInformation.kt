package com.example.healthmyusualtime.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.net.toUri
import com.example.healthmyusualtime.Login
import com.example.healthmyusualtime.R
import com.example.healthmyusualtime.databinding.ActivityUserInformationBinding
import com.example.healthmyusualtime.group.GroupMain
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.Url
import java.io.IOException
import java.util.regex.Pattern

class UserInformation : AppCompatActivity() {
    private val GALLERY = 1
    lateinit var binding: ActivityUserInformationBinding
    lateinit var UserImg : ImageView
    private val namePattern1 = "^[가-힣]{2,8}$"
    //한글로만 이루어진 닉네임 정규 표현식
    private val namePattern2 ="^(?=\\D)[a-zA-Z0-9]{2,12}\$"
    // 영문자, 숫자로만 이루어진 닉네임 정규 표현식

    // 한글 + 숫자 조합으로 정규식 2~10 챗지피티한테 물어보기
    var checkName_finsih = false
    var interList = ArrayList<String>()
    var buttonList = ArrayList<Button>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setViews()

        UserImg.setOnClickListener{
            openGallery()
        }


        binding.checkName.setOnClickListener {
            val userName = binding.inputName.text.toString()
            Toast.makeText(applicationContext,"사용 가능한 닉네임입니다.", Toast.LENGTH_SHORT).show()

//            postCheckName(userName)

            binding.inputName.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkName_finsih = false
                    binding.textCheckName.setText("중복확인을 해주세요")
                }
            })
        }

        binding.UserInfoNext.setOnClickListener{
            val userName = binding.inputName.getText().toString()
            val intent = Intent(this, UserInterset::class.java)
            intent.putExtra("name",userName)
            startActivity(intent)
//            if(checkName_finsih == true) {
//            val intent = Intent(this, UserInterset::class.java)
//            intent.putExtra("name",userName)
//            intent.putExtra("uri",imageuri)
//            startActivity(intent)
//                val userName = binding.inputName.getText().toString()
//                val imageuri = HmutSharedPreferences.getUserImageUrl(this).toUri()
//                val information = UserInfo(userName, interList, imageuri)
//                Log.d("information",information.toString())
////                postUser(information)
//            }else if(checkName_finsih == true) {
//                Toast.makeText(applicationContext,"닉네임 중복확인을 해주세요",Toast.LENGTH_SHORT).show()
//            }
//            else{
//                Toast.makeText(applicationContext,"가입 조건에 맞게 입력해 주세요",Toast.LENGTH_SHORT).show()
//            }
        }

        binding.UserInfoCancel.setOnClickListener(){
            finish()
        }

    }

    fun setViews(){
        UserImg = binding.profileImage
    }
    fun openGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY && resultCode == RESULT_OK && data != null) {
            val imageUri = data.data // 선택된 이미지의 URI
            // 이미지 처리 작업을 수행합니다.
            UserImg.setImageURI(imageUri)
            HmutSharedPreferences.setUserImageUrl(this,imageUri.toString()) // 유저 이미지 저장
        }

    }

    fun patternCheckName(id : String) :Boolean {
        val p1 = Pattern.matches(namePattern1, id) // 한글인 경우
        val p2 = Pattern.matches(namePattern2, id) // 영어+ 숫자 인경우
        return (p1 || p2)
    }
    fun postCheckName(userName: String) {
        val gson = GsonBuilder().create()
        val str = gson.toJson(userName)
        val okHttpClient = OkHttpClient()
        val requestBody = str.toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .method("POST", requestBody)
            .url("")
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                CoroutineScope(Dispatchers.Main).launch{
                    Toast.makeText(applicationContext,"연결 실패", Toast.LENGTH_SHORT).show()
                }
                Log.e("fail",e.message.toString())
                Log.e("fail",e.toString())
            }
            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful) {
                    Log.i("Success", response.message)
                    Log.i("Success", response.toString())
                    val final_Name = binding.inputName.text.toString().trim() // 공백제거
                    CoroutineScope(Dispatchers.Main).launch {
                        when (patternCheckName(final_Name)) {
                            true -> {
                                binding.textCheckName.text = "사용 가능한 이름"
                                checkName_finsih = true
                            }
                            else -> {
                                binding.textCheckName.text = "사용할 수 없는 이름입니다."
                                checkName_finsih = false
                            }
                        }
                    }
                }
                else{
                    Log.e("connection error",response.body.toString())
                    CoroutineScope(Dispatchers.Main).launch {
                        binding.textCheckName.text = "사용중인 이름입니다"
                        checkName_finsih = false
                    }
                }
            }
        })
    }

    override fun onRestart() {
        super.onRestart()
        if(HmutSharedPreferences.getUserName(this).isNotBlank())
            finish()
    }
}

data class UserInfo(var email: String?,
                    var profileImage : String?,
                    var name : String?,
                    var tagValues : List<String>?) {
}
