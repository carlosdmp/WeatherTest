package com.cdmp.data.mapper

import com.cdmp.data.model.WeatherRequestEntity
import com.cdmp.domain.model.WeatherRequest

object WeatherRequestMapper {
    fun WeatherRequest.toRequest(): WeatherRequestEntity = when (this) {
        is WeatherRequest.ZipRequest -> WeatherRequestEntity.ZipCodeRequest(query)
        is WeatherRequest.CityRequest -> WeatherRequestEntity.CityNameRequest(query)
        is WeatherRequest.CoordRequest -> WeatherRequestEntity.GeoCoordRequest(coord.lat, coord.lon)
    }
}