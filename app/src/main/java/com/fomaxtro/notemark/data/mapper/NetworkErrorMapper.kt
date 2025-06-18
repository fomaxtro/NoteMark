package com.fomaxtro.notemark.data.mapper

import com.fomaxtro.notemark.data.remote.util.NetworkError
import com.fomaxtro.notemark.domain.error.LoginError
import com.fomaxtro.notemark.domain.error.RegisterError

fun NetworkError.toRegisterError(): RegisterError {
    return when (this) {
        NetworkError.NO_INTERNET -> RegisterError.NO_INTERNET
        NetworkError.BAD_REQUEST -> RegisterError.INVALID_INPUT
        NetworkError.CONFLICT -> RegisterError.USER_ALREADY_EXIST
        NetworkError.TOO_MANY_REQUEST -> RegisterError.TOO_MANY_ATTEMPTS
        NetworkError.SERVER_ERROR -> RegisterError.SERVICE_UNAVAILABLE
        else -> RegisterError.UNKNOWN
    }
}

fun NetworkError.toLoginError(): LoginError {
    return when (this) {
        NetworkError.NO_INTERNET -> LoginError.NO_INTERNET
        NetworkError.UNAUTHORIZED -> LoginError.INVALID_CREDENTIALS
        NetworkError.TOO_MANY_REQUEST -> LoginError.TOO_MANY_ATTEMPTS
        NetworkError.SERVER_ERROR -> LoginError.SERVICE_UNAVAILABLE
        else -> LoginError.UNKNOWN
    }
}