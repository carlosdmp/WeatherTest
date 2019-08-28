package com.cdmp.domain.datatypes

import java.lang.Exception

fun <L> left(a: L) = Either.Left(a)
fun <R> right(b: R) = Either.Right(b)

fun <A, B, C> ((A) -> B).compose(f: (B) -> C): (A) -> C = {
    f(this(it))
}

sealed class Either<out L, out R> {

    data class Left<out L>(val a: L) : Either<L, Nothing>()
    data class Right<out R>(val b: R) : Either<Nothing, R>()

    fun <C> fold(fnL: (L) -> C, fnR: (R) -> C): C =
        when (this) {
            is Left -> fnL(a)
            is Right -> fnR(b)
        }
}


inline fun <T, L, R> Either<L, R>.flatMap(fn: (R) -> Either<L, T>): Either<L, T> =
    when (this) {
        is Either.Left -> Either.Left(a)
        is Either.Right -> fn(b)
    }

fun <T, L, R> Either<L, R>.map(fn: (R) -> (T)): Either<L, T> = this.flatMap(fn.compose(::right))

fun <P, T, L, R> Either<P, T>.bimap(
    fn: (T) -> R,
    errorFn: (P) -> L
): Either<L, R> = when (this) {
    is Either.Left -> Either.Left(errorFn(a))
    is Either.Right -> Either.Right(fn(b))
}


inline fun <R> safeCall(
    block: () -> R
): Either<Throwable, R> =
    try {
        val response = block()
        right(response)
    } catch (exception: Exception) {
        left(exception)
    }
