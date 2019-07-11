package com.cdmp.weatherapp.data.service

import com.cdmp.weatherapp.data.model.WeatherEntity
import retrofit2.http.GET
import retrofit2.http.Query

//Last retrofit versions allows to use suspend keyword, so it works really well with coroutines
interface WeatherService {

    @GET("/data/2.5/weather")
    suspend fun getWeatherByName(@Query("q") query: String, @Query("APPID") apiKey: String): WeatherEntity

    @GET("/data/2.5/weather")
    suspend fun getWeatherByZip(@Query("zip") query: String, @Query("APPID") apiKey: String): WeatherEntity

    @GET("/data/2.5/weather")
    suspend fun getWeatherByCoord(@Query("lat") lat: String,@Query("lon") lon: String, @Query("APPID") apiKey: String): WeatherEntity
}