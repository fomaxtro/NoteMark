package com.fomaxtro.notemark.presentation.ui

import android.content.Context
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource

sealed interface UiText {
    data class StringResource(
        @field:StringRes val resId: Int,
        val args: Array<Any> = emptyArray()
    ) : UiText {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as StringResource

            if (resId != other.resId) return false
            if (!args.contentEquals(other.args)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = resId
            result = 31 * result + args.contentHashCode()
            return result
        }
    }

    data class DynamicString(val value: String) : UiText
    data class PluralStringResource(
        @field:PluralsRes val resId: Int,
        val count: Int
    ) : UiText

    @Composable
    fun asString(): String {
        return when (this) {
            is StringResource -> stringResource(resId)
            is DynamicString -> value
            is PluralStringResource -> pluralStringResource(resId, count, count)
        }
    }

    fun asString(context: Context): String {
        return when (this) {
            is StringResource -> context.getString(resId)
            is DynamicString -> value
            is PluralStringResource -> context.resources.getQuantityString(resId, count, count)
        }
    }
}