package com.achsanit.gxsales.application

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.achsanit.gxsales.di.KoinInitializer
import com.achsanit.gxsales.utils.TimberInitializer

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        disableDarkTheme()
        KoinInitializer.init(this)
        TimberInitializer.init()
    }

    private fun disableDarkTheme() {
        // code for disable dark theme in application
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}