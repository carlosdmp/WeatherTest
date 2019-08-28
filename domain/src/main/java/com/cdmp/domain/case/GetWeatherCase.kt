package com.cdmp.domain.case

import com.cdmp.domain.CoordCalculator
import com.cdmp.domain.datatypes.Either
import com.cdmp.domain.datatypes.flatMap
import com.cdmp.domain.datatypes.safeCall
import com.cdmp.domain.model.*
import com.cdmp.domain.repository.WeatherRepoContract
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

class GetWeatherCase(private val repo: WeatherRepoContract, private val io: CoroutineDispatcher) {

    suspend fun getWeather(weatherRequest: WeatherRequest): Either<Throwable, List<DomainWeatherPoint>> =
        withContext(io) {
            //First call to get the weather, and handle the possible error
            safeCall { repo.fetchWeather(weatherRequest) }
                .flatMap { originWeather ->
                    safeCall {
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
                        }.awaitAll() + DomainWeatherPoint(Point.ORIGIN, originWeather)
                    }
                    //And finally I add the initial item, and map the possible error to a domain model
                }
        }
}