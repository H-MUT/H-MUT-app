package com.example.healthmyusualtime.group.search

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthmyusualtime.R
import com.example.healthmyusualtime.group.DataGroup
import com.example.healthmyusualtime.group.GroupCreate
import com.example.healthmyusualtime.group.GroupMain


class GroupSearchAdapter(private val context: Context, private  val datalist: MutableList<DataGroup>):
    RecyclerView.Adapter<GroupSearchAdapter.ItemViewHolder>() {

    var mPosition = 0

    private fun setPostion(position : Int){
        mPosition = position
    }

    inner class ItemViewHolder(itemView : View):
        RecyclerView.ViewHolder(itemView){
        val SearchGroupImg = itemView.findViewById<ImageView>(R.id.searchImage)
        val SearchGroupName = itemView.findViewById<TextView>(R.id.searchname)
        val SearchGroupIntro = itemView.findViewById<TextView>(R.id.searchintro)

        fun bind(dataGroup: DataGroup, context: Context){
            if(dataGroup.imageUrl != null){
                val resourceld = context.resources.getIdentifier(dataGroup.imageUrl.toString(),"drawavle",context.packageName)
                if(resourceld > 0)
                    SearchGroupImg.setImageResource(resourceld)
                else
                    SearchGroupImg.setImageResource(R.drawable.img_1)
            }
            else
                SearchGroupImg.setImageResource(R.drawable.img_1)
            SearchGroupName.text = dataGroup.groupName
            SearchGroupIntro.text = dataGroup.introduceMessage

            itemView.setOnClickListener(){
                val intent = Intent(context, GroupIntro::class.java) // 테스트 해보는거 그룹으로 넘어감
                intent.putExtra("imageuri",dataGroup.imageUrl)
                intent.putExtra("name",dataGroup.groupName)
                intent.putExtra("groupid",dataGroup.groupId)
                context.startActivity(intent)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rv_serachgroup,parent,false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(datalist[position], context)
    }

    override fun getItemCount(): Int {
        return datalist.size
    }
}