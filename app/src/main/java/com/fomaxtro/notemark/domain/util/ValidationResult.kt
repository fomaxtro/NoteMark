package com.fomaxtro.notemark.domain.util

sealed interface ValidationResult<out T> {
    data object Success : ValidationResult<Nothing>
    data class Error<T : com.fomaxtro.notemark.domain.util.Error>(val error: T) : ValidationResult<T>
}