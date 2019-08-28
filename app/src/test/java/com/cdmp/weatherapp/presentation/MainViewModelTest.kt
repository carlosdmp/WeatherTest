package com.cdmp.weatherapp.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import arrow.core.right
import com.cdmp.domain.case.GetWeatherCase
import com.cdmp.domain.model.DomainError
import com.cdmp.domain.model.DomainWeatherPoint
import com.cdmp.domain.model.WeatherRequest
import com.cdmp.presentation.MainViewModel
import com.cdmp.presentation.model.ErrorDisplay
import com.cdmp.presentation.model.WeatherDisplay
import com.cdmp.presentation.model.mapper.ErrorDisplayMapper
import com.cdmp.presentation.model.mapper.WeatherDisplayMapper
import com.cdmp.presentation.utils.testScope
import com.cdmp.weatherapp.domain.case.GetWeatherCase
import com.cdmp.weatherapp.domain.model.DomainError
import com.cdmp.weatherapp.domain.model.DomainWeatherPoint
import com.cdmp.weatherapp.domain.model.Point
import com.cdmp.weatherapp.domain.model.WeatherRequest
import com.cdmp.weatherapp.presentation.model.ErrorDisplay
import com.cdmp.weatherapp.presentation.model.WeatherDisplay
import com.cdmp.weatherapp.presentation.model.mapper.ErrorDisplayMapper
import com.cdmp.weatherapp.presentation.model.mapper.WeatherDisplayMapper
import com.cdmp.weatherapp.presentation.utils.testScope
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule

class MainViewModelTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val mockedCase = mockk<GetWeatherCase>()

    private val mockedModel = mockk<List<DomainWeatherPoint>>(relaxed = true)

    private val mockedDisplay = WeatherDisplay(
        highestWind = Point.NORTH to "",
        highestRain = null,
        highestTemperature = Point.ORIGIN to "",
        highestHumidity = Point.WEST to ""
    )

    private val domainError: DomainError = mockk(relaxed = true)

    private val errorDisplay: ErrorDisplay = mockk(relaxed = true)

    private val resultObserver = mockk<Observer<Either<ErrorDisplay, WeatherDisplay>>> {
        every { onChanged(any()) } returns Unit
    }

    private val loadingObserver = mockk<Observer<Boolean>> {
        every { onChanged(any()) } returns Unit
    }

    private val viewModel: MainViewModel by lazy {
        MainViewModel(
            getWeatherCase = mockedCase,
            scope = testScope()
        ).apply {
            weatherInfo.observeForever(resultObserver)
            isLoading.observeForever(loadingObserver)
        }
    }

    @Before
    fun setUp() {
        mockkObject(WeatherDisplayMapper, ErrorDisplayMapper)
        every { WeatherDisplayMapper.transform(mockedModel) } returns mockedDisplay
        every { ErrorDisplayMapper.transform(domainError) } returns errorDisplay
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun initialLiveData() {
        viewModel
        verify { loadingObserver.onChanged(false) }
        assertEquals(viewModel.isLoading.value, false)
        confirmVerified(loadingObserver)
    }

    @Test
    fun citySearchSuccess() {
        val request = WeatherRequest.CityRequest("test")
        coEvery { mockedCase.getWeather(request) } returns Either.right(mockedModel)

        viewModel.onNewSearch(request)

        verifyOrder {
            loadingObserver.onChanged(false)
            loadingObserver.onChanged(true)
            resultObserver.onChanged(Right(mockedDisplay))
            loadingObserver.onChanged(false)
        }
        assertEquals(viewModel.weatherInfo.value, Right(mockedDisplay))
        confirmVerified(resultObserver, loadingObserver)
    }

    @Test
    fun zipSearchSuccess() {
        val request = WeatherRequest.ZipRequest("test")
        coEvery { mockedCase.getWeather(request) } returns Either.right(mockedModel)

        viewModel.onNewSearch(request)

        verifyOrder {
            loadingObserver.onChanged(false)
            loadingObserver.onChanged(true)
            resultObserver.onChanged(Right(mockedDisplay))
            loadingObserver.onChanged(false)
        }
        assertEquals(viewModel.weatherInfo.value, Right(mockedDisplay))
        confirmVerified(resultObserver, loadingObserver)
    }

    @Test
    fun citySearchError() {
        val request = WeatherRequest.CityRequest("test")
        coEvery { mockedCase.getWeather(request) } returns Either.left(domainError)

        viewModel.onNewSearch(request)

        verifyOrder {
            loadingObserver.onChanged(false)
            loadingObserver.onChanged(true)
            resultObserver.onChanged(Left(errorDisplay))
            loadingObserver.onChanged(false)
        }
        assertEquals(viewModel.weatherInfo.value, Left(errorDisplay))
        confirmVerified(resultObserver, loadingObserver)
    }

    @Test
    fun zipSearchError() {
        val request = WeatherRequest.ZipRequest("test")
        coEvery { mockedCase.getWeather(request) } returns Either.left(domainError)

        viewModel.onNewSearch(request)

        verifyOrder {
            loadingObserver.onChanged(false)
            loadingObserver.onChanged(true)
            resultObserver.onChanged(Left(errorDisplay))
            loadingObserver.onChanged(false)
        }
        assertEquals(viewModel.weatherInfo.value, Left(errorDisplay))
        confirmVerified(resultObserver, loadingObserver)
    }

}