package com.fomaxtro.notemark.presentation.screen.registration.mapper

import com.fomaxtro.notemark.R
import com.fomaxtro.notemark.domain.validator.RegistrationDataError
import com.fomaxtro.notemark.presentation.ui.UiText

fun RegistrationDataError.toUiText(): UiText {
    return when (this) {
        RegistrationDataError.USERNAME_TOO_SHORT -> UiText.StringResource(R.string.username_too_short)
        RegistrationDataError.USERNAME_TOO_LONG -> UiText.StringResource(R.string.username_too_long)
        RegistrationDataError.EMAIL_INVALID -> UiText.StringResource(R.string.email_invalid)
        RegistrationDataError.PASSWORD_INVALID -> UiText.StringResource(R.string.password_invalid)
        RegistrationDataError.PASSWORD_NOT_MATCH -> UiText.StringResource(R.string.password_not_match)
    }
}