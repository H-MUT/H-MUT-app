package com.example.healthmyusualtime.login

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.healthmyusualtime.Information

object HmutSharedPreferences {
    private var myAccount : String = "account"

    fun setUserId(context: Context, input: String){
        val prefs : SharedPreferences = context.getSharedPreferences(myAccount, Context.MODE_PRIVATE)
        val editors : SharedPreferences.Editor = prefs.edit()
        editors.putString("my_Id", input)
        editors.commit()
    }
    fun getUserId(context: Context) : String {
        val prefs : SharedPreferences = context.getSharedPreferences(myAccount, Context.MODE_PRIVATE)
        return prefs.getString("my_Id", "").toString()
    }
    fun setUserPw(context: Context, input: String){
        val prefs : SharedPreferences = context.getSharedPreferences(myAccount, Context.MODE_PRIVATE)
        val editors : SharedPreferences.Editor = prefs.edit()
        editors.putString("my_Pw", input)
        editors.commit()
    }
    fun getUserPw(context: Context) : String {
        val prefs : SharedPreferences = context.getSharedPreferences(myAccount, Context.MODE_PRIVATE)
        return prefs.getString("my_Pw", "").toString()
    }
    fun setUserName(context: Context, input: String){
        val prefs : SharedPreferences = context.getSharedPreferences(myAccount, Context.MODE_PRIVATE)
        val editors : SharedPreferences.Editor = prefs.edit()
        editors.putString("my_Name", input)
        editors.commit()
    }
    fun getUserName(context: Context) : String {
        val prefs : SharedPreferences = context.getSharedPreferences(myAccount, Context.MODE_PRIVATE)
        return prefs.getString("my_Name", "").toString()
    }
    fun setUserInterest(context: Context, input: String){
        val prefs : SharedPreferences = context.getSharedPreferences(myAccount, Context.MODE_PRIVATE)
        val editors : SharedPreferences.Editor = prefs.edit()
        editors.putString("my_Interest", input)
        editors.commit()
    }
    fun getUserInterest(context: Context) : String {
        val prefs : SharedPreferences = context.getSharedPreferences(myAccount, Context.MODE_PRIVATE)
        return prefs.getString("my_Interest", "").toString()
    }
    fun clearUser(context: Context){
        val prefs : SharedPreferences = context.getSharedPreferences(myAccount, Context.MODE_PRIVATE)
        val editors : SharedPreferences.Editor = prefs.edit()
        editors.clear()
        editors.commit()
    }
    //로그아웃
    fun clearInfo(context: Context){
        val pref : SharedPreferences = context.getSharedPreferences(myAccount, MODE_PRIVATE)
        val editor : SharedPreferences.Editor = pref.edit()
        editor.remove("my_Id")
        editor.remove("my_Pw")
        editor.commit()
    }


}
class Manager(val context: Context){
    companion object{
        lateinit var information: Information
        fun set(context: Context){
            information = Information(
                HmutSharedPreferences.getUserId(context),
                HmutSharedPreferences.getUserPw(context),
                HmutSharedPreferences.getUserName(context),
                HmutSharedPreferences.getUserInterest(context),
            null)
        }
    }
}