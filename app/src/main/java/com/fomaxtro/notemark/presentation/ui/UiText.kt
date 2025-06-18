package com.fomaxtro.notemark.presentation.ui

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed interface UiText {
    data class StringResource(@StringRes val resId: Int) : UiText

    @Composable
    fun asString(): String {
        return when (this) {
            is StringResource -> stringResource(resId)
        }
    }

    fun asString(context: Context): String {
        return when (this) {
            is StringResource -> context.getString(resId)
        }
    }
}