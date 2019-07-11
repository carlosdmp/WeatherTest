package com.cdmp.weatherapp.di

import com.cdmp.weatherapp.BuildConfig
import com.cdmp.weatherapp.data.WeatherApi
import com.cdmp.weatherapp.data.repository.WeatherRepo
import com.cdmp.weatherapp.data.service.WeatherService
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object DataModules {

    private const val BaseUrl = "http://api.openweathermap.org/"

    val remoteServiceModule = module {
        single {
            OkHttpClient.Builder()
                .connectTimeout(10L, TimeUnit.SECONDS)
                .readTimeout(10L, TimeUnit.SECONDS).build()
        }

        single {
            createWebService<WeatherService>(
                get(),
                BaseUrl
            )
        }
    }

    val apiModule = module {
        single {
            WeatherApi(get(), BuildConfig.ApiKey)
        }
    }

    val repoModule = module {
        single {
            WeatherRepo(get())
        }
    }
}

inline fun <reified T> createWebService(okHttpClient: OkHttpClient, url: String): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    return retrofit.create(T::class.java)
}