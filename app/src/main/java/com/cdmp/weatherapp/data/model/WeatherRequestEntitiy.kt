package com.cdmp.weatherapp.data.model

sealed class WeatherRequestEntity {
    data class CityNameRequest(val query: String) : WeatherRequestEntity()

    data class ZipCodeRequest(val query: String) : WeatherRequestEntity()

    data class GeoCoordRequest(val lat: Double, val lon: Double) : WeatherRequestEntity()
}

