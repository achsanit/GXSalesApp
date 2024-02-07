package com.achsanit.gxsales.utils

import com.achsanit.gxsales.data.local.SharedPreferencesManager
import okhttp3.Interceptor
import okhttp3.Response

class CustomInterceptor(private val sharedPref: SharedPreferencesManager): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = sharedPref.getString(SharedPreferencesManager.TOKEN_KEY)
        val newRequest = chain.request().newBuilder()

        return if (accessToken.isNotEmpty()) {
            newRequest.addHeader("Authorization", "Bearer $accessToken")
            chain.proceed(newRequest.build())
        } else {
            chain.proceed(newRequest.build())
        }

    }
}