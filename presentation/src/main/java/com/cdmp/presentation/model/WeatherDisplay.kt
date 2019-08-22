package com.cdmp.presentation.model

import com.cdmp.domain.model.Point

data class WeatherDisplay(
    val highestTemperature: Pair<Point, String>?,
    val highestHumidity: Pair<Point, String>?,
    val highestRain: Pair<Point, String>?,
    val highestWind: Pair<Point, String>?
)

