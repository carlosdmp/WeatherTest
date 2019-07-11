package com.cdmp.weatherapp.di

import com.cdmp.weatherapp.domain.case.GetWeatherCase
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

object DomainModules {
    val useCaseModules = module {
        single { GetWeatherCase(get(), Dispatchers.IO) }
    }
}