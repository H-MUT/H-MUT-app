package com.example.healthmyusualtime.group

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.example.healthmyusualtime.R
import com.example.healthmyusualtime.databinding.ActivityGroupCreateBinding
import com.example.healthmyusualtime.login.HmutSharedPreferences
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
    private val GALLERY = 1
    lateinit var GroupImg : ImageView
    var bitmap : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.groupInfoCancel.setOnClickListener(){
            finish()
        }
        binding.groupImgbtn.setOnClickListener(){
            openGallery()
        }
        binding.groupComplete.setOnClickListener(){
            val interest = binding.groupInter.selectedItem.toString()
            val groupName = binding.inputGroupName.text.toString()
            val groupInfo = binding.groupInfoText.text.toString()
            val groupManager = HmutSharedPreferences.getUserName(application)
            val groupMem = 1
            val groupImg = binding.groupImage.toString()
            val dataGroup = DataGroup(groupImg,groupManager,groupName,interest,groupInfo,groupMem)
            addGroup(dataGroup)
        }

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
                        GroupImg.setImageResource(R.mipmap.hmutlogo_round)
                    }
                    else{
                        GroupImg.setImageBitmap(bitmap)
                    }
                }
                catch (e:Exception)
                {
                    e.printStackTrace()
                }
            }
        }
    }

    fun addGroup(dataGroup: DataGroup){
        val gson = GsonBuilder().create()
        val str = gson.toJson(dataGroup)
        val okHttpClient = OkHttpClient()
        val requestBody = str.toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .method("POST", requestBody)
            .url("")
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
}