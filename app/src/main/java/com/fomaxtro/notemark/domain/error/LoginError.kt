package com.fomaxtro.notemark.domain.error

import com.fomaxtro.notemark.domain.util.Error

enum class LoginError : Error {
    NO_INTERNET,
    TOO_MANY_ATTEMPTS,
    SERVICE_UNAVAILABLE,
    INVALID_CREDENTIALS,
    UNKNOWN
}