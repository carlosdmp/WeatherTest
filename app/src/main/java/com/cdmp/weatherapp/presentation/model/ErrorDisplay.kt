package com.cdmp.weatherapp.presentation.model


sealed class ErrorDisplay

object NotFoundErrorDisplay : ErrorDisplay()

object BadRequestErrorDisplay : ErrorDisplay()

class UnknownErrorDisplay(val message: String? = null) : ErrorDisplay()

