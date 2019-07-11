package com.cdmp.weatherapp.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import arrow.core.Either
import arrow.core.Left
import arrow.core.Try
import com.cdmp.weatherapp.domain.case.GetWeatherCase
import com.cdmp.weatherapp.domain.model.DomainError
import com.cdmp.weatherapp.domain.model.WeatherRequest
import com.cdmp.weatherapp.presentation.model.ErrorDisplay
import com.cdmp.weatherapp.presentation.model.WeatherDisplay
import com.cdmp.weatherapp.presentation.model.mapper.ErrorDisplayMapper
import com.cdmp.weatherapp.presentation.model.mapper.WeatherDisplayMapper
import com.cdmp.weatherapp.presentation.utils.ViewModelScope
import com.cdmp.weatherapp.presentation.utils.default
import com.cdmp.weatherapp.presentation.utils.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel(
    private val getWeatherCase: GetWeatherCase,
    scope: ViewModelScope = viewModelScope()
) : ViewModel(), ViewModelScope by scope {

    val weatherInfo = MutableLiveData<Either<ErrorDisplay, WeatherDisplay>>()
    val isLoading = MutableLiveData<Boolean>().default(false)

    fun onNewSearch(weatherRequest: WeatherRequest) {
        //Launch a new coroutine in the Android Main dispatcher
        launch {
            isLoading.postValue(true)
            weatherInfo.postValue(
                //Execute the case and check if there was some exception
                Try { getWeatherCase.getWeather(weatherRequest) }.fold({
                    Left(DomainError(it))
                }, { it }).bimap(
                    { error ->
                        ErrorDisplayMapper.transform(error)
                    },
                    { result ->
                        WeatherDisplayMapper.transform(result)
                    })
            )
            isLoading.postValue(false)
        }
    }
}


