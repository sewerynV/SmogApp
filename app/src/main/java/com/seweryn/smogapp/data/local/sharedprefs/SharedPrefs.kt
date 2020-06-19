package com.seweryn.smogapp.data.local.sharedprefs

import android.content.Context
import android.content.SharedPreferences

abstract class SharedPrefs(context: Context) {

    protected val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(preferenceKey(), Context.MODE_PRIVATE)

    protected abstract fun preferenceKey(): String

    protected fun putInt(key: String, value: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    protected fun getInt(key: String): Int {
        return sharedPreferences.getInt(key, 0)
    }

    protected fun putString(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    protected fun getString(key: String): String {
        return sharedPreferences.getString(key, "")
    }
}