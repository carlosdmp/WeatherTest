package com.cdmp.weatherapp.data.model.mapper

import com.cdmp.weatherapp.data.model.WeatherEntity
import com.cdmp.weatherapp.domain.model.Coord
import com.cdmp.weatherapp.domain.model.DomainWeather

object WeatherDomainMapper {
    fun transform(entity: WeatherEntity) = entity.run {
        DomainWeather(
            coord = Coord(coord.lat, coord.lon),
            temp = main.temp,
            humidity = main.humidity,
            windSpeed = wind.speed,
            lastHourRain = rain?.lastHour
        )
    }
}