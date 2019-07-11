package com.cdmp.weatherapp.domain.model

enum class Point { ORIGIN, NORTH, SOUTH, EAST, WEST }

data class Coord(
    val lat: Double,
    val lon: Double
) {
    fun getByNewLat(newLat: Double) = Coord(newLat, lon)
    fun getByNewLon(newLon: Double) = Coord(lat, newLon)
}

data class GeoPoint(val point: Point, val coord: Coord)