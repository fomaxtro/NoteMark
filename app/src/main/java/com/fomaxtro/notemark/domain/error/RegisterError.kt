package com.fomaxtro.notemark.domain.error

import com.fomaxtro.notemark.domain.util.Error

enum class RegisterError : Error {
    NO_INTERNET,
    USER_ALREADY_EXIST,
    INVALID_INPUT,
    TOO_MANY_ATTEMPTS,
    SERVICE_UNAVAILABLE,
    UNKNOWN
}