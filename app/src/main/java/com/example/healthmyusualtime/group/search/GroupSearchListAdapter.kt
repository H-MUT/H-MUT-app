package com.example.healthmyusualtime.group.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthmyusualtime.R
import com.example.healthmyusualtime.group.DataGroup

class GroupSearchListAdapter(private val context: Context, private  val datalist: MutableList<DataSearchGroup>):
    RecyclerView.Adapter<GroupSearchListAdapter.ItemViewHolder>() {

    var mPosition = 0

    private fun setPostion(position : Int){
        mPosition = position
    }

    inner class ItemViewHolder(itemView : View):
        RecyclerView.ViewHolder(itemView){
        val GroupList = itemView.findViewById<RecyclerView>(R.id.rv_groupsearch)
        val Tag = itemView.findViewById<TextView>(R.id.tag)

        fun bind(dataSearchGroup: DataSearchGroup, context: Context){
            Tag.text = "#${dataSearchGroup.title}"
            GroupList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            val adapter = GroupSearchAdapter(context, dataSearchGroup.groupList.toMutableList())
            GroupList.adapter = adapter
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rv_searchgrouplist,parent,false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(datalist[position], context)
    }

    override fun getItemCount(): Int {
        return datalist.size
    }
}