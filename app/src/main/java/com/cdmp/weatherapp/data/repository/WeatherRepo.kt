package com.cdmp.weatherapp.data.repository

import androidx.annotation.WorkerThread
import com.cdmp.weatherapp.data.WeatherApi
import com.cdmp.weatherapp.data.model.mapper.WeatherDomainMapper
import com.cdmp.weatherapp.domain.model.WeatherRequest

class WeatherRepo(private val api: WeatherApi) {

    //Im not using any local persitence or multiple data sources so im just calling the api
    //Also, I am transforming the data model to the domain model
    @WorkerThread
    suspend fun fetchWeather(request: WeatherRequest) =
        WeatherDomainMapper.transform(api.fetchWeather(request.toRequest()))

}