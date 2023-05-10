package com.example.healthmyusualtime

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.healthmyusualtime.databinding.FragmentSearchGroupBinding
import com.example.healthmyusualtime.group.DataGroup
import com.example.healthmyusualtime.group.search.DataSearchGroup
import com.example.healthmyusualtime.group.search.GroupSearchListAdapter
import com.example.healthmyusualtime.group.search.TagViewModel
import com.example.healthmyusualtime.home.HomeGroupAdapter
import com.example.healthmyusualtime.login.HmutSharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SearchGroupFragment : Fragment() {

    lateinit var binding: FragmentSearchGroupBinding
    lateinit var groupRecyclerView : RecyclerView
    lateinit var groupList : ArrayList<DataGroup>
    lateinit var rvset : ArrayList<DataSearchGroup>
    lateinit var  mainActivity: MainActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchGroupBinding.inflate(inflater, container, false)

        groupRecyclerView = binding.RVGroupList
        groupList = ArrayList<DataGroup>()
        groupList.add(DataGroup(null, null,"test1","헬스","매일","test1","우리는 너무 행복해요",2))
        groupList.add(DataGroup(null, null,"test2","헬스","매일","test2","우리는 너무 행복해요",2))
        groupList.add(DataGroup(null, null,"test3","헬스","매일","test3","우리는 너무 행복해요",2))
        groupList.add(DataGroup(null, null,"test4","헬스","매일","test3","우리는 너무 행복해요",2))
        groupList.add(DataGroup(null, null,"test5","헬스","매일","test3","우리는 너무 행복해요",2))
        groupList.toList()
        rvset = ArrayList<DataSearchGroup>()
        rvset.add(DataSearchGroup("헬스",groupList))
        rvset.add(DataSearchGroup("수영",groupList))
        rvset.add(DataSearchGroup("등산",groupList))
        val GroupSearchListRVAdapter = GroupSearchListAdapter(mainActivity, rvset.toMutableList())
        groupRecyclerView.adapter = GroupSearchListRVAdapter


        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

}