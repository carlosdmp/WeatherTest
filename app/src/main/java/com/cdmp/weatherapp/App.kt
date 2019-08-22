package com.cdmp.weatherapp

import android.app.Application
import com.cdmp.weatherapp.di.DiModuleBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

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
