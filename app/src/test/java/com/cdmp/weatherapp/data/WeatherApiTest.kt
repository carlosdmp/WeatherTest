package com.cdmp.weatherapp.data

import com.cdmp.weatherapp.data.model.WeatherEntity
import com.cdmp.weatherapp.data.model.WeatherRequestEntity
import com.cdmp.weatherapp.data.service.WeatherService
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class WeatherApiTest {

    private val service: WeatherService = mockk()
    private val apiKey = "test api key"
    private val query = "test"
    private val api = WeatherApi(service, apiKey)

    @Test
    fun byCity() {
        val response: WeatherEntity = mockk()
        coEvery { service.getWeatherByName(any(), any()) } returns response
        val result = runBlocking {
            api.fetchWeather(WeatherRequestEntity.CityNameRequest(query))
        }
        coVerifyOrder { service.getWeatherByName(query, apiKey) }
        confirmVerified(service)
        assertEquals(response, result)
    }

    @Test
    fun byZip() {
        val response: WeatherEntity = mockk()
        coEvery { service.getWeatherByZip(any(), any()) } returns response
        val result = runBlocking {
            api.fetchWeather(WeatherRequestEntity.ZipCodeRequest(query))
        }
        coVerifyOrder { service.getWeatherByZip(query, apiKey) }
        confirmVerified(service)
        assertEquals(response, result)
    }

    @Test
    fun byCoord() {
        val response: WeatherEntity = mockk()
        coEvery { service.getWeatherByCoord(any(), any(), any()) } returns response
        val result = runBlocking {
            api.fetchWeather(WeatherRequestEntity.GeoCoordRequest(1.0, 1.0))
        }
        coVerifyOrder { service.getWeatherByCoord("1.0","1.0", apiKey) }
        confirmVerified(service)
        assertEquals(response, result)
    }
}