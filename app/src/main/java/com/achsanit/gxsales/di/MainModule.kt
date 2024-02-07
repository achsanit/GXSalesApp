package com.achsanit.gxsales.di

import com.achsanit.gxsales.BuildConfig
import com.achsanit.gxsales.data.MainRepository
import com.achsanit.gxsales.data.local.DataStorePreference
import com.achsanit.gxsales.data.network.service.GxService
import com.achsanit.gxsales.ui.features.DashboardViewModel
import com.achsanit.gxsales.ui.features.login.LoginViewModel
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val mainModule = module {
    single {
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)

        // add chucker interceptor if apps is debug variant
        if (BuildConfig.DEBUG) {
            client.addInterceptor(
                ChuckerInterceptor.Builder(androidContext())
                    .collector(ChuckerCollector(androidContext()))
                    .maxContentLength(250000L)
                    .redactHeaders(emptySet())
                    .alwaysReadResponseBody(true)
                    .build()
            )
        }

        client.build()
    }

    single {
        // instance the service
        val baseUrl = BuildConfig.BASE_URL
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()

        retrofit.create(GxService::class.java)
    }

    single { DataStorePreference(androidContext()) }

    single { MainRepository(get(), get()) }

    viewModel { LoginViewModel(get()) }
    viewModel { DashboardViewModel() }
}