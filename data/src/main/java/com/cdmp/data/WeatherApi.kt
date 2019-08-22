package com.cdmp.weatherapp.data

import androidx.annotation.WorkerThread
import com.cdmp.data.model.WeatherRequestEntity
import com.cdmp.data.model.WeatherRequestEntity.*
import com.cdmp.weatherapp.data.service.WeatherService

class WeatherApi(private val service: WeatherService, private val apiKey: String) {

    //Im using a request with different kinds, because we have different strategies to get the weather from the backend
    //So for that I used a kind of strategy pattern but choosing the functionality in the when instead of by inheritance
    @WorkerThread
    suspend fun fetchWeather(request: WeatherRequestEntity) = when (request) {
        is CityNameRequest -> service.getWeatherByName(request.query, apiKey)
        is ZipCodeRequest -> service.getWeatherByZip(request.query, apiKey)
        is GeoCoordRequest -> service.getWeatherByCoord(request.lat.toString(), request.lon.toString(), apiKey)
    }

}