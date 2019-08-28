package com.cdmp.weatherapp.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.cdmp.domain.case.GetWeatherCase
import com.cdmp.domain.datatypes.Either
import com.cdmp.domain.datatypes.left
import com.cdmp.domain.datatypes.right
import com.cdmp.domain.model.DomainError
import com.cdmp.domain.model.DomainWeatherPoint
import com.cdmp.domain.model.Point
import com.cdmp.domain.model.WeatherRequest
import com.cdmp.presentation.MainViewModel
import com.cdmp.presentation.model.ErrorDisplay
import com.cdmp.presentation.model.WeatherDisplay
import com.cdmp.presentation.model.mapper.ErrorDisplayMapper
import com.cdmp.presentation.model.mapper.WeatherDisplayMapper
import com.cdmp.presentation.utils.testScope
import io.mockk.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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
        coEvery { mockedCase.getWeather(request) } returns right(mockedModel)

        viewModel.onNewSearch(request)

        verifyOrder {
            loadingObserver.onChanged(false)
            loadingObserver.onChanged(true)
            resultObserver.onChanged(right(mockedDisplay))
            loadingObserver.onChanged(false)
        }
        assertEquals(viewModel.weatherInfo.value, right(mockedDisplay))
        confirmVerified(resultObserver, loadingObserver)
    }

    @Test
    fun zipSearchSuccess() {
        val request = WeatherRequest.ZipRequest("test")
        coEvery { mockedCase.getWeather(request) } returns right(mockedModel)

        viewModel.onNewSearch(request)

        verifyOrder {
            loadingObserver.onChanged(false)
            loadingObserver.onChanged(true)
            resultObserver.onChanged(right(mockedDisplay))
            loadingObserver.onChanged(false)
        }
        assertEquals(viewModel.weatherInfo.value, right(mockedDisplay))
        confirmVerified(resultObserver, loadingObserver)
    }

    @Test
    fun citySearchError() {
        val request = WeatherRequest.CityRequest("test")
        coEvery { mockedCase.getWeather(request) } returns left(domainError)

        viewModel.onNewSearch(request)

        verifyOrder {
            loadingObserver.onChanged(false)
            loadingObserver.onChanged(true)
            resultObserver.onChanged(left(errorDisplay))
            loadingObserver.onChanged(false)
        }
        assertEquals(viewModel.weatherInfo.value, left(errorDisplay))
        confirmVerified(resultObserver, loadingObserver)
    }

    @Test
    fun zipSearchError() {
        val request = WeatherRequest.ZipRequest("test")
        coEvery { mockedCase.getWeather(request) } returns left(domainError)

        viewModel.onNewSearch(request)

        verifyOrder {
            loadingObserver.onChanged(false)
            loadingObserver.onChanged(true)
            resultObserver.onChanged(left(errorDisplay))
            loadingObserver.onChanged(false)
        }
        assertEquals(viewModel.weatherInfo.value, left(errorDisplay))
        confirmVerified(resultObserver, loadingObserver)
    }

}