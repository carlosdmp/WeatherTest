package com.cdmp.weatherapp.di

import com.cdmp.presentation.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


object PresentationModules {
    val mainModule = module {
        viewModel { MainViewModel(getWeatherCase = get()) }
    }
}

