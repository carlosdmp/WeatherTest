package com.cdmp.presentation.model.mapper

import com.cdmp.domain.model.DomainError
import com.cdmp.presentation.model.BadRequestErrorDisplay
import com.cdmp.presentation.model.NotFoundErrorDisplay
import com.cdmp.presentation.model.UnknownErrorDisplay
import retrofit2.HttpException

object ErrorDisplayMapper {
    fun transform(error: DomainError) = when (val cause = error.cause) {
        is HttpException -> when (cause.code()) {
            404 -> NotFoundErrorDisplay
            400 -> BadRequestErrorDisplay
            else -> UnknownErrorDisplay(cause.message)
        }
        else -> UnknownErrorDisplay(cause.message)
    }
}