package com.example.healthmyusualtime.group

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.healthmyusualtime.R
import com.example.healthmyusualtime.databinding.ActivityGroupCreateBinding
import com.example.healthmyusualtime.login.HmutSharedPreferences
import com.example.healthmyusualtime.login.UserInformation
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class GroupCreate : AppCompatActivity() {

    lateinit var binding : ActivityGroupCreateBinding
    var imageUri : Uri? = null
    private val GALLERY = 1
    var checkName_finish : Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("test","그룹 페이지")
        super.onCreate(savedInstanceState)
        binding = ActivityGroupCreateBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.groupInfoCancel.setOnClickListener(){
            finish()
        }

        binding.groupImage.setOnClickListener(){
            openGallery()
        }
        binding.checkGroupName.setOnClickListener(){
            val groupName = binding.inputGroupName.text.toString()
            checkGroupName(groupName)
        }

        binding.groupComplete.setOnClickListener(){
            val interest = binding.groupInter.selectedItem.toString()
            val groupName = binding.inputGroupName.text.toString()
            val groupInfo = binding.groupInfoText.text.toString()
            val groupManager = HmutSharedPreferences.getUserName(application)
            val groupMem = 1
            val groupfrequency = binding.groupfrequency.selectedItem.toString()
            val groupLongInfo = binding.groupInfolongText.toString()
            val groupImg = binding.groupImage.toString().toUri()
            val dataGroup = DataGroup(groupImg,groupManager,groupName,interest,groupfrequency,groupInfo,groupLongInfo,groupMem,null)
            addGroup(dataGroup)
//            if(checkName_finish == true){
//                addGroup(dataGroup)
//            }
        }

        val GroupSpinner: Spinner = binding.groupInter
        ArrayAdapter.createFromResource(
            this,
            R.array.interest_array, android.R.layout.simple_spinner_dropdown_item
        ).also{
                adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            GroupSpinner.adapter = adapter
        }
        val TimeSpinner: Spinner = binding.groupfrequency
        ArrayAdapter.createFromResource(
            this,
            R.array.frequency_array, android.R.layout.simple_spinner_dropdown_item
        ).also{
                adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            TimeSpinner.adapter = adapter
        }

    }
    fun openGallery(){
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, GALLERY)
        Log.d("test","갤러리 오픈")
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("test","갤러리 이미지 선택")
        if (requestCode == GALLERY && resultCode == RESULT_OK && data != null) {
            imageUri = data.data!!
            Log.d("test",imageUri.toString())
            binding.groupImage.setImageURI(imageUri)
        }
    }

    fun addGroup(dataGroup: DataGroup){
        val gson = GsonBuilder().create()
        val str = gson.toJson(dataGroup)
        val okHttpClient = OkHttpClient()
        val requestBody = str.toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .method("POST", requestBody)
            .url("http://3.36.163.92/api/groups")
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                CoroutineScope(Dispatchers.Main).launch{
                    Toast.makeText(applicationContext,"Fail to create group",Toast.LENGTH_SHORT).show()
                }
                Log.e("fail to create grpup",e.message.toString())
                Log.e("fail to create group",e.toString())
            }
            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful) {
                    Log.d("Success to create group",response.message)
                    Log.d("Success to create group",response.toString())
                    CoroutineScope(Dispatchers.Main).launch{
                        Toast.makeText(applicationContext,"Success to create group",Toast.LENGTH_SHORT).show()
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
    fun checkGroupName(name: String){
        val gson = GsonBuilder().create()
        val str = gson.toJson(name)
        val okHttpClient = OkHttpClient()
        val requestBody = str.toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .method("GET", requestBody)
            .url("http://3.36.163.92/api/groups/check-name")
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                CoroutineScope(Dispatchers.Main).launch{
                    Toast.makeText(applicationContext,"연결 실패",Toast.LENGTH_SHORT).show()
                }
                Log.e("fail",e.message.toString())
                Log.e("fail",e.toString())
            }
            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful) {
                    Log.i("Success", response.message)
                    Log.i("Success", response.toString())
                    checkName_finish = true
                }
                else{
                    Log.e("connection error",response.body.toString())
                    checkName_finish = false
                }
            }
        })
    }
}