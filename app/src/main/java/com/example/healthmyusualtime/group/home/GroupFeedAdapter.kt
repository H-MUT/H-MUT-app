package com.example.healthmyusualtime.group.home

import android.animation.ValueAnimator
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.healthmyusualtime.R
import com.example.healthmyusualtime.login.HmutSharedPreferences
import com.example.healthmyusualtime.login.Manager
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class GroupFeedAdapter (private val context: Context, private val dataList: MutableList<Datafeed>, private val visible:Boolean
):RecyclerView.Adapter<GroupFeedAdapter.ItemViewHolder>() {

    var mPosition = 0

    private fun setPosition(position:Int){
        mPosition = position
    }

    inner class ItemViewHolder(itemView: View):
        RecyclerView.ViewHolder(itemView){
        private val Homeuserimg = itemView.findViewById<ImageView>(R.id.FeedUserImg)
        private val Homeusername = itemView.findViewById<TextView>(R.id.FeedUserName)
        private val Homecontentimg = itemView.findViewById<ImageView>(R.id.FeedContentImg)
        private val Homecontent = itemView.findViewById<TextView>(R.id.FeedContent)
        var LikeNum = itemView.findViewById<TextView>(R.id.likeNum)
        private var Like_btn = itemView.findViewById<LottieAnimationView>(R.id.likebtn)

        fun bind(datafeed: Datafeed){
            if(!visible){
                itemView.findViewById<Toolbar>(R.id.Feed_menu_toolbar).visibility = View.GONE
            }
            if(datafeed.writer.profileImage !=null){
//                val a = datafeed.writer.profileImage.toUri()
//                Homeuserimg.setImageURI(datafeed.writer.profileImage.toUri())
            }
            else{
                Homeuserimg.setImageResource(R.mipmap.ic_launcher_round)
            }
            if(datafeed.saveUrl !=null){
                Glide.with(context)
                    .load("")
                    .into(Homecontentimg)
            }
            else{
                Homecontentimg.setImageResource(R.mipmap.ic_launcher_round)
            }
            Homeusername.text = datafeed.writer.name
            Homecontent.text = datafeed.content
            LikeNum.text = datafeed.likeNum.toString()
            if(datafeed.isLiked == true){
                val animator = ValueAnimator.ofFloat(0.5f,0.5f).setDuration(10)
                animator.addUpdateListener { animation: ValueAnimator ->
                    Like_btn.setProgress(
                        animation.getAnimatedValue() as Float
                    )
                }
                animator.start()
            }
            else{
                val animator = ValueAnimator.ofFloat(0f,0f).setDuration(10)
                animator.addUpdateListener { animation: ValueAnimator ->
                    Like_btn.setProgress(
                        animation.getAnimatedValue() as Float
                    )
                }
                animator.start()
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupFeedAdapter.ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rv_groupfeed,parent, false)
        return ItemViewHolder(view)
    }
    override fun onBindViewHolder(holder: GroupFeedAdapter.ItemViewHolder, position: Int) {
        val position = position
        holder.bind(dataList[position])
        Log.d("test",dataList[position].isLiked.toString())
        val like_btn = holder.itemView.findViewById<LottieAnimationView>(R.id.likebtn)
        val username = HmutSharedPreferences.getUserName(context)
        val dataHomeFeed = dataList[position]


        holder.itemView.findViewById<Toolbar>(R.id.Feed_menu_toolbar).setOnMenuItemClickListener(){
            when (it.itemId) {
                R.id.feed_delete ->
                    if(username == dataHomeFeed.writer.name){
                        feedDelete(dataHomeFeed)
                    }
                R.id.feed_revise ->
                    if(username == dataHomeFeed.writer.name){
                        feedRevise(dataHomeFeed,position)

                    }
            }
            true

        }
        like_btn.setOnClickListener {
            if(dataList[position].isLiked == false){
                val gson = GsonBuilder().create()
                val str = gson.toJson(dataList[position])
                val okHttpClient = OkHttpClient()
                val requestBody = str.toRequestBody("application/json".toMediaTypeOrNull())
                val request = Request.Builder()
                    .method("POST", requestBody)
                    .url("")
                    .build()
                okHttpClient.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.d("fail to like",e.message.toString())
                        Log.d("fail to like",e.toString())
                    }
                    override fun onResponse(call: Call, response: Response) {
                        if(response.isSuccessful) {
                            Log.d("Success to like",response.message)
                            Log.d("Success to like",response.toString())

                            CoroutineScope(Dispatchers.Main).launch{
                                holder.LikeNum.text = response.body.string()

                                val animator = ValueAnimator.ofFloat(0f,0.5f).setDuration(1000)
                                animator.addUpdateListener { animation: ValueAnimator ->
                                    like_btn.setProgress(
                                        animation.getAnimatedValue() as Float
                                    )
                                }
                                animator.start()
                            }
                            dataList[position].isLiked = true

                        }
                        else{
                            Log.d("fail to connection like",response.message)
                            Log.d("fail to connection like",response.toString())

                        }
                    }
                })
//                holder.itemView.likeNum++
            } else{
                val gson = GsonBuilder().create()
                val str = gson.toJson(dataList[position])
                val okHttpClient = OkHttpClient()
                val requestBody = str.toRequestBody("application/json".toMediaTypeOrNull())
                val request = Request.Builder()
                    .method("DELETE", requestBody)
                    .url("")
                    .build()
                okHttpClient.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.d("fail to delete like",e.toString())
                    }
                    override fun onResponse(call: Call, response: Response) {
                        if(response.isSuccessful) {
                            Log.d("Success to delete feed",response.toString())
                            CoroutineScope(Dispatchers.Main).launch{
                                holder.LikeNum.text = response.body.string()
                                val animator = ValueAnimator.ofFloat(0.5f,1f).setDuration(1000)
                                animator.addUpdateListener { animation: ValueAnimator ->
                                    like_btn.setProgress(
                                        animation.getAnimatedValue() as Float
                                    )
                                }
                                animator.start()
                            }
                            dataList[position].isLiked = false
                        }
                        else{
                            Log.d("fail to connection(delete feed)",response.toString())

                        }
                    }
                })
            }
        }
//
    }
    override fun getItemCount(): Int {
        return dataList.size
    }
    fun feedDelete(datafeed: Datafeed){
        val feedId = datafeed.feedId
        val gson = GsonBuilder().create()
        val str = gson.toJson(feedId)
        val okHttpClient = OkHttpClient()
        val requestBody = str.toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .method("DELETE", requestBody)
            .url("")
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("fail to delete feed",e.message.toString())
                Log.e("fail to delete feed",e.toString())
            }
            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful) {
                    Log.d("Delete to feed",response.message)
                    Log.d("Delete to feed",response.toString())
                    (context as Activity).finish()
                }
                else{
                    Log.e("fail to connection",response.message)
                    Log.e("fail to connection",response.toString())

                }
            }

        })
    }


    fun feedRevise(datafeed: Datafeed,position: Int){
        val intent = Intent(context, FeedRevise::class.java)
        intent.putExtra("content", datafeed.content)
        intent.putExtra("feedid",datafeed.feedId)
        context.startActivity(intent)

    }


}