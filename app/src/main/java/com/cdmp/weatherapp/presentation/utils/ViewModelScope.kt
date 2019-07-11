package com.cdmp.weatherapp.presentation.utils

import kotlinx.coroutines.*

interface ViewModelScope : CoroutineScope {

    val UI: CoroutineDispatcher
    val IO: CoroutineDispatcher

    val job: Job
}

fun viewModelScope() = object : ViewModelScope {
    override val UI = Dispatchers.Main
    override val IO = Dispatchers.IO

    override val job = Job()
    override val coroutineContext by lazy { UI + job }
}

fun testScope() = object : ViewModelScope {
    override val UI = Dispatchers.Unconfined
    override val IO = Dispatchers.Unconfined

    override val job = Job()
    override val coroutineContext by lazy { UI + job }
}