package com.fomaxtro.notemark.domain.util

sealed interface Result<out D, out E> {
    data class Success<D>(val data: D) : Result<D, Nothing>
    data class Error<E : com.fomaxtro.notemark.domain.util.Error>(val error: E) : Result<Nothing, E>
}

inline fun <D, E: Error, R> Result<D, E>.map(map: (D) -> R): Result<R, E> {
    return when (this) {
        is Result.Success -> Result.Success(map(data))
        is Result.Error -> Result.Error(error)
    }
}

inline fun <D, E: Error, R: Error> Result<D, E>.mapError(map: (E) -> R): Result<D, R> {
    return when (this) {
        is Result.Success -> Result.Success(data)
        is Result.Error -> Result.Error(map(error))
    }
}

fun <D, E: Error> Result<D, E>.asEmptyResult(): EmptyResult<E> {
    return map {  }
}

typealias EmptyResult<E> = Result<Unit, E>