package com.cdmp.weatherapp.domain.model

import com.cdmp.weatherapp.data.model.WeatherRequestEntity

sealed class WeatherRequest {

    class ZipRequest(val query: String) : WeatherRequest()

    class CityRequest(val query: String) : WeatherRequest()

    class CoordRequest(val coord: Coord) : WeatherRequest()

    fun toRequest(): WeatherRequestEntity = when (this) {
        is ZipRequest -> WeatherRequestEntity.ZipCodeRequest(query)
        is CityRequest -> WeatherRequestEntity.CityNameRequest(query)
        is CoordRequest -> WeatherRequestEntity.GeoCoordRequest(coord.lat, coord.lon)
    }
}

