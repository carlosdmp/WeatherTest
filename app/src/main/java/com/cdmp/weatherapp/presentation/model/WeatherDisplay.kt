package com.cdmp.weatherapp.presentation.model

import com.cdmp.weatherapp.domain.model.Point

data class WeatherDisplay(
    val highestTemperature: Pair<Point, String>?,
    val highestHumidity: Pair<Point, String>?,
    val highestRain: Pair<Point, String>?,
    val highestWind: Pair<Point, String>?
)

