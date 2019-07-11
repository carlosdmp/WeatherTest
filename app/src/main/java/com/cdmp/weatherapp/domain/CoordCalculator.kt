package com.cdmp.weatherapp.domain

import com.cdmp.weatherapp.domain.model.Coord
import com.cdmp.weatherapp.domain.model.GeoPoint
import com.cdmp.weatherapp.domain.model.Point

object CoordCalculator {
    private const val earth_radius = 6370000.0
    private const val distance = 200000.0

    fun getAllCoords(origin: Coord): List<GeoPoint> {
        val diffCoord = Coord(
            lat = (distance / earth_radius) * (180 / Math.PI),
            lon = (distance / earth_radius) * (180 / Math.PI) / Math.cos(origin.lat * Math.PI / 180)
        )

        return listOf(
            GeoPoint(Point.NORTH, Coord(origin.lat + diffCoord.lat, origin.lon)),
            GeoPoint(Point.SOUTH, Coord(origin.lat - diffCoord.lat, origin.lon)),
            GeoPoint(Point.EAST, Coord(origin.lat, origin.lon + diffCoord.lon)),
            GeoPoint(Point.WEST, Coord(origin.lat, origin.lon - diffCoord.lon))
        )
    }


}