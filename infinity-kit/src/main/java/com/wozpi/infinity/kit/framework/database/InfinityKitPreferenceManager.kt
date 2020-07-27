package com.wozpi.infinity.kit.framework.database

import android.content.Context

open class InfinityKitPreferenceManager(context: Context, prefsName:String? = null,
                                        private val keyAuthorization:String? = null) {
    companion object{
        const val PREFS_NAME = "prefs_drama_cool_android"
        const val KEY_AUTHORIZATION = "DRAMA_COOL_AUTHORIZATION"
    }
    private val mPreference = context.getSharedPreferences(prefsName?:PREFS_NAME, Context.MODE_PRIVATE)

    fun getToken():String? = getValue(keyAuthorization?:KEY_AUTHORIZATION)

    fun setToken(token:String) : Boolean = putStringValue(keyAuthorization?:KEY_AUTHORIZATION,token)

    fun removeToken() : Boolean = removeValue(keyAuthorization?:KEY_AUTHORIZATION)

    fun getValue(key:String):String? = mPreference.getString(key,null)

    fun putStringValue(key:String,value:String):Boolean = mPreference.edit()?.putString(key, value)?.commit()?:false

    fun removeValue(key:String):Boolean = mPreference.edit()?.remove(key)?.commit()?:false
}