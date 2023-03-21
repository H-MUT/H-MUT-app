package com.example.healthmyusualtime.login

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.example.healthmyusualtime.R
import com.example.healthmyusualtime.databinding.ActivityUserInformationBinding

class UserInformation : AppCompatActivity() {
    private val GALLERY = 1
    lateinit var binding: ActivityUserInformationBinding
    lateinit var openGalleryBtn : Button
    lateinit var UserImg : ImageView
    var bitmap : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setViews()

        openGalleryBtn.setOnClickListener{
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent,GALLERY)
        }
    }

    fun setViews(){
        openGalleryBtn = binding.profilebtn
        UserImg = binding.profileImage
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
}