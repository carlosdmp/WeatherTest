package com.cdmp.domain.model


sealed class WeatherRequest {

    class ZipRequest(val query: String) : WeatherRequest()

    class CityRequest(val query: String) : WeatherRequest()

    class CoordRequest(val coord: Coord) : WeatherRequest()


}

