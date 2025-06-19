package com.fomaxtro.notemark.presentation.screen.login.mapper

import com.fomaxtro.notemark.R
import com.fomaxtro.notemark.domain.error.LoginError
import com.fomaxtro.notemark.presentation.ui.UiText

fun LoginError.toUiText(): UiText {
    return when (this) {
        LoginError.NO_INTERNET -> UiText.StringResource(R.string.no_internet)
        LoginError.TOO_MANY_ATTEMPTS -> UiText.StringResource(R.string.too_many_attempts)
        LoginError.SERVICE_UNAVAILABLE -> UiText.StringResource(R.string.service_unavailable)
        LoginError.INVALID_CREDENTIALS -> UiText.StringResource(R.string.invalid_credentials)
        LoginError.UNKNOWN -> UiText.StringResource(R.string.unknown_error)
    }
}