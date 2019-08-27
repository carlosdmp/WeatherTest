package com.cdmp.weatherapp.di

import com.cdmp.domain.case.GetWeatherCase
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

object DomainModules {
    val useCaseModules = module {
        single { GetWeatherCase(get(named("real")), Dispatchers.IO) }
    }
}