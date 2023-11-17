package com.example.healthmyusualtime

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity.CENTER
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.healthmyusualtime.databinding.FragmentSearchGroupBinding
import com.example.healthmyusualtime.databinding.FragmentUserBinding
import com.example.healthmyusualtime.login.HmutSharedPreferences
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserFragment : Fragment() {
    private val GALLERY = 1
    lateinit var binding: FragmentUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment

        binding.month.setUsePercentValues(true)
        binding.total.setUsePercentValues(true)
        val dayEntry = ArrayList<PieEntry>()
        dayEntry.add(PieEntry(950f,"달성"))
        dayEntry.add(PieEntry(150f,"미달성"))
        val weekEntry = ArrayList<PieEntry>()
        weekEntry.add(PieEntry(400f,"달성"))
        weekEntry.add(PieEntry(1000f,"미달성"))

        val pieDayDateSet = PieDataSet(dayEntry,"")
        pieDayDateSet.setColors(Color.parseColor("#329d00"),Color.parseColor("#Cfd0d0"))
        pieDayDateSet.apply{
            valueTextColor = Color.BLACK
            valueTextSize = 15f
        }
        val pieWeekDataSet = PieDataSet(weekEntry,"")
        pieWeekDataSet.setColors(Color.parseColor("#1561bb"),Color.parseColor("#Cfd0d0"))
        pieWeekDataSet.apply{
            valueTextColor = Color.BLACK
            valueTextSize = 15f
        }
        val piedayData = PieData(pieDayDateSet)
        binding.month.apply{
            data = piedayData
            description.isEnabled = false
            isRotationEnabled = false
            centerText = "이달의 달성률"
        }
        val pieweekData = PieData(pieWeekDataSet)
        binding.total.apply{
            data = pieweekData
            description.isEnabled = false
            isRotationEnabled = false
            centerText = "총 달성률"
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val topMenuToolbar = view.findViewById<Toolbar>(R.id.top_menu_toolbar)

        topMenuToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.user_setting_logout -> {
//                    MySharedPreferences.clearUser(requireContext())
                    HmutSharedPreferences.clearInfo(requireContext())
                    activity?.let {
                        val intent = Intent(context, Login::class.java)
                        startActivity(intent)
                        activity?.supportFragmentManager
                            ?.beginTransaction()
                            ?.remove(this)
                            ?.commit()
                    }
                }
                R.id.user_setting_changeUsername -> {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle("닉네임 변경")
                    builder.setIcon(R.mipmap.ic_launcher_round)

                    val v1 = layoutInflater.inflate(R.layout.dialog_user_name, null)
                    builder.setView(v1)

                    val listener = DialogInterface.OnClickListener{ p0, p1 ->
                        val alert = p0 as AlertDialog
                        val edit1 : EditText? = alert.findViewById(R.id.wantUsername)
                        val wantName = edit1?.text.toString()
                        binding.settingUserName.text = wantName
                        binding.rateText.text = "${wantName} 님의 운동활동"
                    }
                    builder.setPositiveButton("확인", listener)
                    builder.setNegativeButton("취소", null)
                    builder.show()
                }
                R.id.user_setting_changeUserImg -> {
                    openGallery()
                }
                R.id.user_setting_changeTag -> {

                }
            }
            true
        }
    }
    fun openGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            val imageUri = data.data // 선택된 이미지의 URI
            // 이미지 처리 작업을 수행합니다.
            val UserImg = binding.settingUserImg
            UserImg.setImageURI(imageUri)
        }

    }

}