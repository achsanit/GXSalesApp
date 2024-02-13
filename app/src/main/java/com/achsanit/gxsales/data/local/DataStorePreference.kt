package com.achsanit.gxsales.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map

class DataStorePreference(private val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_PREFERENCE_NAME)

    suspend fun isLogin(): Boolean {
        val preferences = context.dataStore.data.first()
        return preferences[IS_LOGIN_KEY] ?: false
    }

    suspend fun getToken(): String {
        return context.dataStore.data.map { pref ->
            pref[TOKEN_KEY] ?: ""
        }.last()
    }

    suspend fun setLoginData(token: String) {
        context.dataStore.edit { pref ->
            pref[IS_LOGIN_KEY] = true
            pref[TOKEN_KEY] = token
        }
    }

    suspend fun setLogoutData() {
        context.dataStore.edit { pref ->
            pref[IS_LOGIN_KEY] = false
            pref[TOKEN_KEY] = ""
        }
    }

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("user_token_key")
        private val IS_LOGIN_KEY = booleanPreferencesKey("is_login_key")
        const val DATA_STORE_PREFERENCE_NAME = "data_preference"
    }
}