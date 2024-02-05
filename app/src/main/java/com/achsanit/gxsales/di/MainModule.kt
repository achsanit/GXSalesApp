package com.achsanit.gxsales.di

import com.achsanit.gxsales.ui.features.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {

    viewModel { LoginViewModel() }
}