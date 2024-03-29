package com.cdmp.weatherapp.domain.case

import arrow.core.Either
import arrow.core.Try
import arrow.core.flatMap
import com.cdmp.weatherapp.data.model.WeatherRequestEntity.*
import com.cdmp.weatherapp.data.model.mapper.WeatherDomainMapper
import com.cdmp.weatherapp.data.repository.WeatherRepo
import com.cdmp.weatherapp.domain.CoordCalculator
import com.cdmp.weatherapp.domain.model.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

typealias WeatherResult = Either<DomainError, List<DomainWeatherPoint>>

class GetWeatherCase(private val repo: WeatherRepo, private val io: CoroutineDispatcher) {

    suspend fun getWeather(weatherRequest: WeatherRequest): WeatherResult =
        withContext(io) {
            //First call to get the weather, and handle the possible error
            Try { repo.fetchWeather(weatherRequest) }.toEither { t -> DomainError(t) }
                .flatMap { originWeather ->
                    Try {
                        //Get the 4 other zones
                        CoordCalculator.getAllCoords(
                            Coord(
                                lat = originWeather.coord.lat,
                                lon = originWeather.coord.lon
                            )
                        ).map { geoPoint ->
                            //map the list of zones, to a list of deferred calls to the repository
                            async {
                                DomainWeatherPoint(
                                    geoPoint.point,
                                    repo.fetchWeather(
                                        WeatherRequest.CoordRequest(geoPoint.coord)
                                    )
                                )
                            }
                        }.map {
                            //I map the deferred list to the actual items list, by waiting since they are all completed
                            it.await()
                        } + DomainWeatherPoint(Point.ORIGIN, originWeather)
                        //And finally I add the initial item, and map the possible error to a domain model
                    }.toEither { t -> DomainError(t) }
                }
        }
}