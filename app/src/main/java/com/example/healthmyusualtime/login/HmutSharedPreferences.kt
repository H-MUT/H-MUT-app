package com.example.healthmyusualtime.login

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

object HmutSharedPreferences {
    private var myAccount : String = "account"

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
    fun setUserMainInterest(context: Context, input: String){
        val prefs : SharedPreferences = context.getSharedPreferences(myAccount, Context.MODE_PRIVATE)
        val editors : SharedPreferences.Editor = prefs.edit()
        editors.putString("main_Interest", input)
        editors.commit()
    }
    fun getUserMainInterest(context: Context) : String {
        val prefs : SharedPreferences = context.getSharedPreferences(myAccount, Context.MODE_PRIVATE)
        return prefs.getString("main_Interest", "").toString()
    }
    fun setUserSubInterest(context: Context, input: String){
        val prefs : SharedPreferences = context.getSharedPreferences(myAccount, Context.MODE_PRIVATE)
        val editors : SharedPreferences.Editor = prefs.edit()
        editors.putString("sub_Interest", input)
        editors.commit()
    }
    fun getUserSubInterest(context: Context) : String {
        val prefs : SharedPreferences = context.getSharedPreferences(myAccount, Context.MODE_PRIVATE)
        return prefs.getString("sub_Interest", "").toString()
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
        lateinit var userInfo: UserInfo
        fun set(context: Context){
            userInfo = UserInfo(
                HmutSharedPreferences.getUserName(context),
                HmutSharedPreferences.getUserMainInterest(context),
                HmutSharedPreferences.getUserSubInterest(context),
            null)
        }
    }
}