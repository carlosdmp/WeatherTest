package com.cdmp.weatherapp.presentation.model.mapper

import com.cdmp.weatherapp.domain.model.DomainWeatherPoint
import com.cdmp.weatherapp.presentation.model.WeatherDisplay
import java.util.Locale.filter
import kotlin.Double.Companion.NEGATIVE_INFINITY

object WeatherDisplayMapper {
    private const val KELVIN_CELSIUS_DIF = 273.15
    private const val FORMAT = "%.2f"
    private const val PERCENTAGE = "%"

    fun transform(zones :List<DomainWeatherPoint>)=
        WeatherDisplay(
            highestTemperature = zones.maxBy { it.weather.temp }?.let { zone ->
                zone.point to String.format(FORMAT, zone.weather.temp.toCelsius())
            },
            highestHumidity =  zones.maxBy { it.weather.humidity }?.let { zone ->
                zone.point to String.format(FORMAT, zone.weather.humidity) + PERCENTAGE
            },
            highestRain =  zones.filter { it.weather.lastHourRain != null }.let { zonesWithRain ->
                when {
                    zonesWithRain.isEmpty() -> null
                    else -> zonesWithRain.maxBy { it.weather.lastHourRain ?: NEGATIVE_INFINITY }?.let { zone ->
                        zone.point to String.format(FORMAT, (zone.weather.lastHourRain ?: NEGATIVE_INFINITY))
                    }
                }
            },
            highestWind =  zones.maxBy { it.weather.windSpeed }?.let { zone ->
                zone.point to String.format(FORMAT, zone.weather.windSpeed)
            }
        )

    private fun Double.toCelsius() = this - KELVIN_CELSIUS_DIF

}