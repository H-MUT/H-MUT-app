package com.example.healthmyusualtime.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.healthmyusualtime.group.DataGroup
import com.example.healthmyusualtime.group.GroupMain
import com.example.healthmyusualtime.R

class HomeGroupAdapter(private val context: Context, private  val datalist: MutableList<DataGroup>):
    RecyclerView.Adapter<HomeGroupAdapter.ItemViewHolder>() {

        var mPosition = 0

    private fun setPostion(position : Int){
        mPosition = position
    }

    inner class ItemViewHolder(itemView : View):
        RecyclerView.ViewHolder(itemView){
            val HomeGroupImg = itemView.findViewById<ImageView>(R.id.HomeGroupImage)
            val HomeGroupName = itemView.findViewById<TextView>(R.id.HomeGroupName)
            val HomeGroupMember = itemView.findViewById<TextView>(R.id.HomeGroupMember)
            val HomeGroupIntro = itemView.findViewById<TextView>(R.id.HomeGroupIntro)
            val HomeGroupFre = itemView.findViewById<TextView>(R.id.HomeGroupFrequncy)
            val HomeGroupInter = itemView.findViewById<TextView>(R.id.HomeGroupInter)
        fun bind(dataGroup: DataGroup, context: Context){
                if(dataGroup.imageUrl != null){
                    val resourceld = context.resources.getIdentifier(dataGroup.imageUrl.toString(),"drawable",context.packageName)
                    if(resourceld > 0)
                        Glide.with(itemView).load(dataGroup.imageUrl).into(HomeGroupImg)
                    else
                        HomeGroupImg.setImageResource(R.drawable.img_1)
                }
                else
                    HomeGroupImg.setImageResource(R.drawable.img_1)
                HomeGroupIntro.text = dataGroup.introduceMessage
                HomeGroupMember.text = dataGroup.groupMember.toString()
                HomeGroupName.text = dataGroup.groupName
                HomeGroupFre.text = "#${dataGroup.groupFrequency}"
                HomeGroupInter.text  = "#${dataGroup.tags}"

                itemView.setOnClickListener(){
                    val intent = Intent(context, GroupMain::class.java) // 테스트 해보는거 그룹으로 넘어감
                    intent.putExtra("groupName",dataGroup.groupName)
                    context.startActivity(intent)
                }
            }

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rv_homegroup,parent,false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(datalist[position], context)
    }

    override fun getItemCount(): Int {
        return datalist.size
    }
}