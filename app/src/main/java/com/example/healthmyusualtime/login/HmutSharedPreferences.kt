package com.example.healthmyusualtime.login

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

object HmutSharedPreferences {
    private var myAccount : String = "account"
    private val prefsFilename = "token_prefs"


    fun setAccessToekn(context: Context, input: String){
        val prefs : SharedPreferences = context.getSharedPreferences(prefsFilename, Context.MODE_PRIVATE)
        val editors : SharedPreferences.Editor = prefs.edit()
        editors.putString("accessToken", input)
        editors.commit()
    }

    fun getAccessToken(context: Context) : String{
        val prefs : SharedPreferences = context.getSharedPreferences(prefsFilename,Context.MODE_PRIVATE)
        return prefs.getString("accessToken","").toString()
    }
    fun setReFreshToekn(context: Context, input: String){
        val prefs : SharedPreferences = context.getSharedPreferences(prefsFilename, Context.MODE_PRIVATE)
        val editors : SharedPreferences.Editor = prefs.edit()
        editors.putString("reFreshToken", input)
        editors.commit()
    }

    fun getReFreshToken(context: Context) : String{
        val prefs : SharedPreferences = context.getSharedPreferences(prefsFilename,Context.MODE_PRIVATE)
        return prefs.getString("reFreshToken","").toString()
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

    fun setUserInterest(context: Context, input: List<String>){
        val prefs : SharedPreferences = context.getSharedPreferences(myAccount, Context.MODE_PRIVATE)
        val editors : SharedPreferences.Editor = prefs.edit()
        for(i in input.indices) {
            val key = "Interest" + (i + 1)
            editors.putString(key, input[i])
        }
        editors.apply()
    }
    fun getUserInterest(context: Context): List<String> {
        val prefs: SharedPreferences = context.getSharedPreferences(myAccount, Context.MODE_PRIVATE)
        val interestList = mutableListOf<String>()
        var i = 1
        var inter = prefs.getString("Interest$i", null)
        while (inter != null) {
            interestList.add(inter)
            i++
            inter = prefs.getString("Interest$i", null)
        }
        return interestList
    }
    fun setUserImageUrl(context: Context, input: String){
        val prefs : SharedPreferences = context.getSharedPreferences(myAccount, Context.MODE_PRIVATE)
        val editors : SharedPreferences.Editor = prefs.edit()
        editors.putString("imageUrl", input)
        editors.commit()
    }
    fun getUserImageUrl(context: Context) : String {
        val prefs : SharedPreferences = context.getSharedPreferences(myAccount, Context.MODE_PRIVATE)
        return prefs.getString("imageUrl", "").toString()
    }
    fun Logout_User(context: Context){
        val prefs : SharedPreferences = context.getSharedPreferences(myAccount, Context.MODE_PRIVATE)
        val pref : SharedPreferences = context.getSharedPreferences(prefsFilename, Context.MODE_PRIVATE)
        val editors : SharedPreferences.Editor = prefs.edit()
        val editor : SharedPreferences.Editor = pref.edit()
        editors.clear()
        editors.commit()
        editor.clear()
        editor.commit()
    }
    //로그아웃
    fun clearInfo(context: Context){
        val prefs : SharedPreferences = context.getSharedPreferences(myAccount, MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.remove("my_Id")
        editor.remove("my_Pw")
        editor.commit()
    }
    // 회원탈퇴


}
class Manager(val context: Context){
    companion object{
        lateinit var userInfo: UserInfo
        fun set(context: Context){
            userInfo = UserInfo(null,
                HmutSharedPreferences.getUserName(context),
                null,
            null)
        }
    }
}
