package com.example.healthmyusualtime.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.healthmyusualtime.group.DataGroup
import com.example.healthmyusualtime.MainActivity

import com.example.healthmyusualtime.R
import com.example.healthmyusualtime.SearchGroupFragment
import com.example.healthmyusualtime.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    lateinit var groupList : ArrayList<DataGroup>
    lateinit var  mainActivity: MainActivity
    lateinit var  homeGroupRV: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        homeGroupRV = binding.RVHomeGroup
        groupList = ArrayList<DataGroup>()
        groupList.add(DataGroup(null,"언더아머수호반","헬스","3:1000이 목표",8))
        groupList.add(DataGroup(null,"우리동네필라테스","필라테스","우당탕탕 필라테스 도전기",5))
        if (groupList.size != 0) {
            binding.noGroup.setVisibility(View.INVISIBLE)
            binding.searchBtn.setVisibility(View.INVISIBLE)
        }
        val homeGroupRVAdapter = HomeGroupAdapter(mainActivity, groupList.toMutableList())
        homeGroupRV.adapter = homeGroupRVAdapter
        // Inflate the layout for this fragment
        binding.searchBtn.setOnClickListener(){
            mainActivity.findViewById<BottomNavigationView>(R.id.bottom_bar).itemIconTintList = ContextCompat.getColorStateList(mainActivity, R.color.color_search_group)
            mainActivity.findViewById<BottomNavigationView>(R.id.bottom_bar).itemTextColor  = ContextCompat.getColorStateList(mainActivity, R.color.color_search_group)
            mainActivity.changeFragment(
                SearchGroupFragment()
            )
        }

        return binding.root

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    fun setRecyclerView(){
        val homeGroupAdapter = HomeGroupAdapter(mainActivity, groupList.toMutableList())
        homeGroupRV.adapter = homeGroupAdapter
    }
}