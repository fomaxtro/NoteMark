package com.fomaxtro.notemark.presentation.screen.registration.mapper

import com.fomaxtro.notemark.R
import com.fomaxtro.notemark.domain.error.RegisterError
import com.fomaxtro.notemark.presentation.ui.UiText

fun RegisterError.toUiText(): UiText {
    return when (this) {
        RegisterError.NO_INTERNET -> UiText.StringResource(R.string.no_internet)
        RegisterError.USER_ALREADY_EXIST -> UiText.StringResource(R.string.user_already_exist)
        RegisterError.INVALID_INPUT -> UiText.StringResource(R.string.invalid_input)
        RegisterError.TOO_MANY_ATTEMPTS -> UiText.StringResource(R.string.too_many_attempts)
        RegisterError.SERVICE_UNAVAILABLE -> UiText.StringResource(R.string.service_unavailable)
        RegisterError.UNKNOWN -> UiText.StringResource(R.string.unknown_error)
    }
}