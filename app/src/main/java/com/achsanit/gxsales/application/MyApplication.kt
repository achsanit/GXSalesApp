package com.achsanit.gxsales.application

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        disableDarkTheme()
    }

    private fun disableDarkTheme() {
        // code for disable dark theme in application
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}