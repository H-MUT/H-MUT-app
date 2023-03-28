package com.example.healthmyusualtime.login

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import com.example.healthmyusualtime.R
import com.example.healthmyusualtime.databinding.ActivityUserInformationBinding
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.regex.Pattern

class UserInformation : AppCompatActivity() {
    private val GALLERY = 1
    lateinit var binding: ActivityUserInformationBinding
    lateinit var openGalleryBtn : Button
    lateinit var UserImg : ImageView
    var bitmap : Bitmap? = null
    private val namePattern1 = "^[가-힣]{2,8}$"
    //한글로만 이루어진 닉네임 정규 표현식
    private val namePattern2 ="^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,12})\$"
    // 영문자, 숫자로만 이루어진 닉네임 정규 표현식
    var checkName_finsih = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setViews()

        openGalleryBtn.setOnClickListener{
            openGallery()
        }

        val mainSpinner: Spinner = binding.mainInter
        val subSpinner: Spinner = binding.subInter
        ArrayAdapter.createFromResource(
            this,
            R.array.interest_array, android.R.layout.simple_spinner_dropdown_item
        ).also{
                adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            mainSpinner.adapter = adapter
            subSpinner.adapter = adapter
        }

        binding.checkName.setOnClickListener {
            val userName = binding.inputName.text.toString()
            val information = UserInfo(userName,"","",null)
            postCheckName(information)

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

        binding.UserInfoComplete.setOnClickListener{
            if(checkName_finsih == true) {
                val userName = binding.inputName.getText().toString()
                val mainInterest = mainSpinner.toString()
                val subInterest = subSpinner.toString()
                val information = UserInfo(userName, mainInterest, subInterest, null)
                // 유저 이미지 받아온거 저장을 해야되는데 흠...
                Log.d("information",information.toString())
                postUser(information)
            }else if(checkName_finsih == true) {
                Toast.makeText(applicationContext,"닉네임 중복확인을 해주세요",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(applicationContext,"가입 조건에 맞게 입력해 주세요",Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun setViews(){
        openGalleryBtn = binding.profilebtn
        UserImg = binding.profileImage
    }

    fun openGallery(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        startActivityForResult(intent,GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){
            if( requestCode ==  GALLERY)
            {
                val imageData: Uri? = data?.data
                Toast.makeText(this,imageData.toString(), Toast.LENGTH_LONG ).show()
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageData)
                    if(bitmap == null){
                        UserImg.setImageResource(R.mipmap.hmutlogo_round)
                    }
                    else{
                        UserImg.setImageBitmap(bitmap)
                    }
                }
                catch (e:Exception)
                {
                    e.printStackTrace()
                }
            }
        }
    }

    fun patternCheckName(id : String) :Boolean {
        val p1 = Pattern.matches(namePattern1, id) // 한글인 경우
        val p2 = Pattern.matches(namePattern2, id) // 영어+ 숫자 인경우
        return (p1 || p2)
    }
    fun postCheckName(information: UserInfo) {
        val gson = GsonBuilder().create()
        val str = gson.toJson(information)
        val okHttpClient = OkHttpClient()
        val requestBody = str.toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .method("POST", requestBody)
            .url("주소")
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

    fun postUser(information: UserInfo) {
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
                    Toast.makeText(applicationContext,"실패",Toast.LENGTH_SHORT).show()
                }
                Log.e("fail",e.message.toString())
                Log.e("fail",e.toString())
            }
            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful) {
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
}

data class UserInfo(var name: String?,
                    var mainInter: String?,
                    var subInter: String?,
                    var userImage : ByteArray?) {
}