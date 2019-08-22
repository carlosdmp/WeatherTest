package com.cdmp.domain.model


data class DomainWeather(
    val coord: Coord,
    val temp: Double,
    val humidity: Double,
    val windSpeed: Double,
    val lastHourRain: Double?
)