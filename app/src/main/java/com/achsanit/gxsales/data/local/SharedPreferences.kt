package com.achsanit.gxsales.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SharedPreferencesManager(private val context: Context) {

    private val preference: SharedPreferences =
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun getBoolean(key: String): Boolean = preference.getBoolean(key, false)
    fun getInt(key: String): Int = preference.getInt(key, 0)
    fun getString(key: String): String = preference.getString(key, "") ?: ""

    fun saveData(key: String, data: Any) {
        when(data) {
            is String -> {
                preference.edit {
                    putString(key, data)
                }
            }
            is Boolean -> {
                preference.edit {
                    putBoolean(key, data)
                }
            }
            is Int -> {
                preference.edit {
                    putInt(key, data)
                }
            }
        }
    }

    companion object {
        const val PREFERENCES_NAME = "com.achsanit.gxsales.preferences"
        const val TOKEN_KEY = "com.achsanit.gxsales.token.key"
    }
}