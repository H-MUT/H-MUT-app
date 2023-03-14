package com.example.healthmyusualtime

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.example.healthmyusualtime.databinding.ActivitySignUpBinding
import java.util.regex.Pattern

class Sign_Up : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    private val pwPattern = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[!@#$%^&*])[A-Za-z0-9!@#$%^&*]{8,15}\$"
    //무조건 숫자, 문자, 특수문자 하나씩 포함하는 정규 표현식
    private val idPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})\$"
    // 영문자, 숫자로만 이루어진 아이디 정규 표현식
    private val namePattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})\$"
    // 영문자, 숫자로만 이루어진 닉네임 정규 표현식
    var checkID_finish = false
    var checkName_finsih = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        var view = binding.root
        setContentView(view)
        var samePW : Boolean

        binding.BackToLogin.setOnClickListener {
            finish()
        }
        // 이전 버튼
        binding.inputPW.addTextChangedListener(object : TextWatcher {           // 비밀번호 기입란
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {              // 비밀번호 조건 안 맞는 경우
                val password = p0?.trim().toString() // 공백 제거
                binding.textInputPW.text = when (patternCheckPw(password)) {
                    true -> "사용 가능한 비밀번호"
                    else -> "양식이 맞지 않는 비밀번호"
                }
                binding.textCheckPW.text = ""
            }
        })

        binding.checkPW.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(checkPassword: Editable?) {
                samePW = checkPassword.toString() == binding.inputPW.text.toString()
                if (samePW) {
                    binding.textCheckPW.setText("비밀번호가 일치합니다")

                } else {
                    binding.textCheckPW.setText("비밀번호가 일치하지 않습니다")
                }
                if (binding.inputPW.length() < 8) {
                    binding.textCheckPW.setText("")
                }
            }
        })
        binding.checkID.setOnClickListener {
            val userID = binding.inputID.text.toString()
            val information = Information(userID,"","","",null)
            postCheckId(information)

            binding.inputID.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkID_finish = false
                    binding.textCheckID.setText("중복확인을 해주세요")
                }
            })
        }

        binding.checkName.setOnClickListener {
            val userName = binding.inputName.text.toString()
            val information = Information("","",userName,"",null)
            postCheckName(information)

            binding.inputID.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkName_finsih = false
                    binding.textCheckID.setText("중복확인을 해주세요")
                }
            })
        }

        binding.signUpBtn.setOnClickListener {
            samePW = binding.inputPW.text.toString() == binding.checkPW.text.toString()
            if((patternCheckPw(binding.inputPW.text.toString()))&& samePW && (checkID_finish)
                && (binding.inputName.length()>=2)) { // && (inputNumber.length() == 11)
                val userID = binding.inputID.getText().toString()
                val userPW = binding.inputPW.getText().toString()
                val userName = binding.inputName.getText().toString()
                val information = Information(userID, userPW, userName, null,null)
                Log.d("information",information.toString())
                postUser(information)
            }else if(!checkID_finish) {
                Toast.makeText(applicationContext,"이메일 중복확인을 해주세요",Toast.LENGTH_SHORT).show()
            }
            else if(!checkName_finsih){
                Toast.makeText(applicationContext,"닉네임 중복확인을 해주세요",Toast.LENGTH_SHORT).show()
            }
            else{
                if(binding.inputName.length()<2) {
                    binding.textCheckName.setText("2자리 이상의 이름을 입력하세요")
                    binding.inputName.addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(p0: Editable?) {
                            if(binding.inputName.length() >= 2)
                                binding.textCheckName.setText("")
                            else{
                                binding.textCheckName.setText("2자리 이상의 이름을 입력하세요")
                            }
                        }
                        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        }
                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        }
                    })
                }
                else {
                    binding.textCheckName.setText("")
                    if(!patternCheckPw(binding.inputPW.text.toString())){
                        Toast.makeText(applicationContext,"checkPw",Toast.LENGTH_SHORT).show()
                        Toast.makeText(applicationContext,"${binding.checkPW.text} , ${binding.inputPW.text}",Toast.LENGTH_SHORT).show()
                    }
                    else if(samePW == false){
                        Toast.makeText(applicationContext,"samePw",Toast.LENGTH_SHORT).show()
                    }
                }
                Toast.makeText(applicationContext,"가입 조건에 맞게 입력해 주세요",Toast.LENGTH_SHORT).show()
            }
        }

    }


    fun patternCheckPw(pw : String):Boolean{
        val p = Pattern.matches(pwPattern, pw)      // 패턴 일치여부 확인
        return p
    }

    fun patternCheckId(id : String) :Boolean {
        val d = Pattern.matches(idPattern, id)      // 패턴 일치여부 확인
        return d
    }
    fun patternCheckName(id : String) :Boolean {
        val d = Pattern.matches(namePattern, id)      // 패턴 일치여부 확인
        return d
    }
    fun postCheckId(information: Information) {
//        val gson = GsonBuilder().create()
//        val str = gson.toJson(information)
//        val okHttpClient = OkHttpClient()
//        val requestBody = str.toRequestBody("application/json".toMediaTypeOrNull())
//        val request = Request.Builder()
//            .method("POST", requestBody)
//            .url("주소")
//            .build()
//        okHttpClient.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                CoroutineScope(Dispatchers.Main).launch{
//                    Toast.makeText(applicationContext,"연결 실패", Toast.LENGTH_SHORT).show()
//                }
//                Log.e("fail",e.message.toString())
//                Log.e("fail",e.toString())
//            }
//            override fun onResponse(call: Call, response: Response) {
//                if(response.isSuccessful) {
//                    Log.i("Success", response.message)
//                    Log.i("Success", response.toString())
//                    val final_Id = binding.inputID.text.toString().trim() // 공백제거
//                    CoroutineScope(Dispatchers.Main).launch {
//                        when (patternCheckId(final_Id)) {
//                            true -> {
//                                binding.textCheckID.text = "사용 가능한 이메일"
//                                checkID_finish = true
//                            }
//                            else -> {
//                                binding.textCheckID.text = "올바른 형식의 이메일을 입력해주세요"
//                                checkID_finish = false
//                            }
//                        }
//                    }
//                }
//                else{
//                    Log.e("connection error",response.body.toString())
//                    CoroutineScope(Dispatchers.Main).launch {
//                        binding.textCheckID.text = "사용중인 이메일입니다"
//                        checkID_finish = false
//                    }
//                }
//            }
//        })
    }
    fun postCheckName(information: Information) {
//        val gson = GsonBuilder().create()
//        val str = gson.toJson(information)
//        val okHttpClient = OkHttpClient()
//        val requestBody = str.toRequestBody("application/json".toMediaTypeOrNull())
//        val request = Request.Builder()
//            .method("POST", requestBody)
//            .url("주소")
//            .build()
//        okHttpClient.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                CoroutineScope(Dispatchers.Main).launch{
//                    Toast.makeText(applicationContext,"연결 실패", Toast.LENGTH_SHORT).show()
//                }
//                Log.e("fail",e.message.toString())
//                Log.e("fail",e.toString())
//            }
//            override fun onResponse(call: Call, response: Response) {
//                if(response.isSuccessful) {
//                    Log.i("Success", response.message)
//                    Log.i("Success", response.toString())
//                    val final_Name = binding.inputName.text.toString().trim() // 공백제거
//                    CoroutineScope(Dispatchers.Main).launch {
//                        when (patternCheckName(final_Name)) {
//                            true -> {
//                                binding.textCheckName.text = "사용 가능한 이름"
//                                checkName_finsih = true
//                            }
//                            else -> {
//                                binding.textCheckName.text = "사용할 수 없는 이름입니다."
//                                checkName_finsih = false
//                            }
//                        }
//                    }
//                }
//                else{
//                    Log.e("connection error",response.body.toString())
//                    CoroutineScope(Dispatchers.Main).launch {
//                        binding.textCheckName.text = "사용중인 이름입니다"
//                        checkName_finsih = false
//                    }
//                }
//            }
//        })
    }

    fun postUser(information: Information) {
//        val gson = GsonBuilder().create()
//        val str = gson.toJson(information)
//        val okHttpClient = OkHttpClient()
//        val requestBody = str.toRequestBody("application/json".toMediaTypeOrNull())
//        val request = Request.Builder()
//            .method("POST", requestBody)
//            .url("주소")
//            .build()
//        okHttpClient.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                CoroutineScope(Dispatchers.Main).launch{
//                    Toast.makeText(applicationContext,"실패",Toast.LENGTH_SHORT).show()
//                }
//                Log.e("fail",e.message.toString())
//                Log.e("fail",e.toString())
//            }
//            override fun onResponse(call: Call, response: Response) {
//                if(response.isSuccessful) {
//                    Log.i("Success", response.message)
//                    Log.i("Success", response.toString())
//                    finish()
//                    }
//                else{
//                    Log.e("connection error",response.body.toString())
//                    }
//                }
//            })
    }
}



data class Information(var userId: String, var password: String, var name: String, var interest: String?, var userImage : ByteArray?) {
}