package com.cdmp.weatherapp

import android.app.Application
import com.cdmp.weatherapp.data.WeatherApi
import com.cdmp.weatherapp.data.repository.WeatherRepo
import com.cdmp.weatherapp.data.service.WeatherService
import com.cdmp.weatherapp.di.DiModuleBuilder
import com.cdmp.weatherapp.di.createWebService
import com.cdmp.weatherapp.domain.case.GetWeatherCase
import com.cdmp.weatherapp.presentation.MainViewModel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(DiModuleBuilder.buildModules())
        }
    }
}
