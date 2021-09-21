package com.example.owes.utils

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.internal.synchronized

object OwesSharedPrefs {


    lateinit var sharedPrefs: SharedPreferences

    fun initSharedPrefs(context: Context) {
        sharedPrefs = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    }

    fun saveBooleanToSharedPrefs(isOn: Boolean) {
        val editor = sharedPrefs.edit()
        with(editor) {
            putBoolean("isSwitched", isOn)
            apply()
        }
    }

    fun saveStringToSharedPrefs(string: String) {
        val editor = sharedPrefs.edit()
        with(editor) {
            putString("string", string)
            apply()
        }
    }

    fun readFromPrefs(key: String, value: String): String? {
        return sharedPrefs.getString(key, value)
    }


    fun deleteFromPrefs(key: String) {
        sharedPrefs.edit().remove(key).apply()
    }

}