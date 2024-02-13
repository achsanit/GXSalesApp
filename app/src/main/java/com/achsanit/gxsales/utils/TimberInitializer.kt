package com.achsanit.gxsales.utils

import com.achsanit.gxsales.BuildConfig
import timber.log.Timber

object TimberInitializer {

    fun init() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}