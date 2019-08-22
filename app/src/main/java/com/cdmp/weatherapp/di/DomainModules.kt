package com.cdmp.weatherapp.di

import com.cdmp.data.repository.WeatherRepo
import com.cdmp.domain.case.GetWeatherCase
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

object DomainModules {
    val useCaseModules = module {
        single { GetWeatherCase(WeatherRepo(get()), Dispatchers.IO) }
    }
}