package com.cdmp.data.repository

import androidx.annotation.WorkerThread
import com.cdmp.data.mapper.WeatherDomainMapper
import com.cdmp.data.mapper.WeatherRequestMapper
import com.cdmp.domain.model.DomainWeather
import com.cdmp.domain.model.WeatherRequest
import com.cdmp.domain.repository.WeatherRepoContract
import com.cdmp.weatherapp.data.WeatherApi

class WeatherRepo(private val api: WeatherApi) : WeatherRepoContract {

    //Im not using any local persitence or multiple data sources so im just calling the api
    //Also, I am transforming the data model to the domain model
    @WorkerThread
    override suspend fun fetchWeather(request: WeatherRequest) =
        WeatherDomainMapper.transform(api.fetchWeather(with(WeatherRequestMapper) { request.toRequest() }))

}