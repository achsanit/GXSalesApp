package com.achsanit.gxsales.application

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.achsanit.gxsales.di.KoinInitializer

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        disableDarkTheme()
        KoinInitializer.init(this)
    }

    private fun disableDarkTheme() {
        // code for disable dark theme in application
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}