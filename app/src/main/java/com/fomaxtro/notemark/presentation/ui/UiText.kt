package com.fomaxtro.notemark.presentation.ui

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed interface UiText {
    data class StringResource(@field:StringRes val resId: Int) : UiText
    data class DynamicString(val value: String) : UiText

    @Composable
    fun asString(): String {
        return when (this) {
            is StringResource -> stringResource(resId)
            is DynamicString -> value
        }
    }

    fun asString(context: Context): String {
        return when (this) {
            is StringResource -> context.getString(resId)
            is DynamicString -> value
        }
    }
}