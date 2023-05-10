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
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
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
    val MAX_SELECTION = 3
    var interList = ArrayList<String>()
    var buttonList = ArrayList<Button>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchGroupBinding.inflate(inflater, container, false)
        makebtn()
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

        binding.interSearch.setOnClickListener(){
            binding.RVGroupList.visibility = View.INVISIBLE
            binding.searchBtn.visibility = View.VISIBLE
            binding.previousBtn.visibility = View.VISIBLE
            binding.buttonContainer.visibility = View.VISIBLE
        }

        binding.searchBtn.setOnClickListener(){
            binding.RVGroupList.visibility = View.VISIBLE
            binding.searchBtn.visibility = View.INVISIBLE
            binding.previousBtn.visibility = View.INVISIBLE
            binding.buttonContainer.visibility = View.INVISIBLE
        }

        binding.previousBtn.setOnClickListener(){
            binding.RVGroupList.visibility = View.VISIBLE
            binding.searchBtn.visibility = View.INVISIBLE
            binding.previousBtn.visibility = View.INVISIBLE
            binding.buttonContainer.visibility = View.INVISIBLE
        }
        return binding.root
    }

    fun makebtn(){
        val tmpList = ArrayList<String>()
        tmpList.add("헬스")
        tmpList.add("필라테스")
        tmpList.add("1")
        tmpList.add("2")
        tmpList.add("3")
        // 여기에 서버 통신으로 리스트를 받아올 예정
        val linearLayout = binding.buttonContainer // 버튼들을 추가할 LinearLayout

        for (i in 0 until tmpList.size) {
            if (i % 3 == 0) { // 3개씩 배치하기 위해 나머지가 0일 때마다 새로운 LinearLayout 추가
                val newLinearLayout = LinearLayout(requireContext())
                newLinearLayout.orientation = LinearLayout.HORIZONTAL
                newLinearLayout.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                linearLayout.addView(newLinearLayout)
            }

            // 버튼 추가
            val newButton = Button(requireContext())
            newButton.setBackgroundResource(R.drawable.round_button)
            buttonList.add(newButton)
            newButton.text = tmpList[i]
            newButton.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
            ) // 3개의 버튼을 동일한 너비로 배치하기 위해 weight를 1로 설정
            val currentLinearLayout = linearLayout.getChildAt(linearLayout.childCount - 1) as LinearLayout
            currentLinearLayout.addView(newButton)
            newButton.setOnClickListener {
                selectButton(newButton)
            }
        }
    }

    fun selectButton(button: Button) {
        button.isSelected = !button.isSelected
        if (button.isSelected) {
            // 선택된 버튼 개수가 MAX_SELECTION을 넘어가면 선택을 해제
            if (getSelectedCount() > MAX_SELECTION) {
                button.isSelected = false
                interList.remove(button.text.toString())
                return
            }
            // 선택된 버튼의 값을 interlist에 추가
            interList.add(button.text.toString())
            button.setBackgroundResource(R.drawable.select_button)

        } else {
            // 선택 취소된 버튼의 값을 interlist에서 제거
            interList.remove(button.text.toString())
            button.setBackgroundResource(R.drawable.round_button)
        }
    }

    // 선택된 버튼 개수 반환 함수
    fun getSelectedCount(): Int {
        var count = 0
        for (button in buttonList) {
            if (button.isSelected) count++
        }
        return count
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

}