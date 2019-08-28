package com.cdmp.domain

import com.cdmp.domain.case.GetWeatherCase
import com.cdmp.domain.datatypes.bimap
import com.cdmp.domain.datatypes.left
import com.cdmp.domain.datatypes.map
import com.cdmp.domain.datatypes.right
import com.cdmp.domain.model.*
import com.cdmp.domain.repository.WeatherRepoContract
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetWeatherCaseTest {

    private val repo: WeatherRepoContract = mockk()

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

        assertEquals(right(2), result.map { it.size })
        assertEquals(right(DomainWeatherPoint(Point.NORTH, secondResult)), result.map { it[0] })
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

        assertEquals(left(error), result.bimap({ it }, { it.cause }))
        confirmVerified(repo)
    }
}
