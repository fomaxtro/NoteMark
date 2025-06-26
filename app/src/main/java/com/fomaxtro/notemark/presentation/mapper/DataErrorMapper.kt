package com.fomaxtro.notemark.presentation.mapper

import com.fomaxtro.notemark.R
import com.fomaxtro.notemark.domain.error.DataError
import com.fomaxtro.notemark.presentation.ui.UiText

fun DataError.toUiText(): UiText {
    return when (this) {
        DataError.DISK_FULL -> UiText.StringResource(R.string.disk_full)
    }
}