package com.cdmp.data.mapper

import com.cdmp.domain.model.DomainWeather
import com.cdmp.data.model.WeatherEntity
import com.cdmp.domain.model.Coord

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