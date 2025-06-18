package com.fomaxtro.notemark.data.remote.util

import com.fomaxtro.notemark.domain.util.Error

enum class NetworkError : Error {
    NO_INTERNET,
    SERIALIZATION,
    UNKNOWN,
    BAD_REQUEST,
    UNAUTHORIZED,
    NOT_FOUND,
    CONFLICT,
    TOO_MANY_REQUEST,
    SERVER_ERROR
}