package com.cdmp.weatherapp.domain

import arrow.core.Left
import arrow.core.Right
import com.cdmp.weatherapp.data.repository.WeatherRepo
import com.cdmp.weatherapp.domain.case.GetWeatherCase
import com.cdmp.weatherapp.domain.model.*
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetWeatherCaseTest {

    private val repo: WeatherRepo = mockk()

    private val case = GetWeatherCase(repo, Dispatchers.Unconfined)

    private val firstZoneCoord = Coord(0.0, 0.0)
    private val firstResult = DomainWeather(firstZoneCoord, 0.0, 0.0, 0.0, 0.0)
    private val secondZoneCoord = Coord(1.0, 1.0)

    private val additionalZones = listOf(
        GeoPoint(Point.NORTH, secondZoneCoord)
    )

    @Before
    fun setUp() {
        mockkObject(CoordCalculator)
        coEvery { CoordCalculator.getAllCoords(firstZoneCoord) } returns additionalZones
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun getWeather() {
        val firstRequest = WeatherRequest.CityRequest("test")

        val secondResult: DomainWeather = mockk(relaxed = true)

        coEvery { repo.fetchWeather(firstRequest) } returns firstResult
        coEvery { repo.fetchWeather(any<WeatherRequest.CoordRequest>()) } returns secondResult

        val result = runBlocking {
            case.getWeather(firstRequest)
        }
        coVerifyOrder {
            repo.fetchWeather(firstRequest)
            repo.fetchWeather(any<WeatherRequest.CoordRequest>())
        }

        assertTrue(result.isRight())
        assertEquals(Right(2), result.map { it.size })
        assertEquals(Right(DomainWeatherPoint(Point.NORTH, secondResult)), result.map { it[0] })
        confirmVerified(repo)
    }

    @Test
    fun getWeatherError() {
        val firstRequest = WeatherRequest.CityRequest("test")

        val error = RuntimeException()

        coEvery { repo.fetchWeather(firstRequest) } throws error

        val result = runBlocking {
            case.getWeather(firstRequest)
        }
        coVerifyOrder {
            repo.fetchWeather(firstRequest)
        }

        assertTrue(result.isLeft())
        assertEquals(Left(error), result.mapLeft { it.cause })
        confirmVerified(repo)
    }
}
