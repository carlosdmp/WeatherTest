package com.cdmp.domain.repository

import com.cdmp.domain.model.DomainWeather
import com.cdmp.domain.model.WeatherRequest


interface WeatherRepoContract {
    suspend fun fetchWeather(request: WeatherRequest): DomainWeather
}