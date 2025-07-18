package com.fomaxtro.notemark.presentation.ui

import com.fomaxtro.notemark.domain.util.ValidationError
import com.fomaxtro.notemark.domain.util.ValidationResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalCoroutinesApi::class)
fun <T, E : ValidationError> createFieldValidationFlow(
    shouldValidate: Flow<Boolean>,
    value: Flow<T>,
    validator: (T) -> ValidationResult<E>,
    onError: (error: E?) -> Unit
): Flow<Boolean> {
    return shouldValidate
        .filter { it }
        .flatMapLatest { value.distinctUntilChanged() }
        .map { validator(it) }
        .onEach { validationResult ->
            if (validationResult is ValidationResult.Error) {
                onError(validationResult.error)
            } else {
                onError(null)
            }
        }
        .map { it is ValidationResult.Success }
}

fun createIsErrorFlow(
    value: Flow<String>,
    isFocused: Flow<Boolean>,
    isValid: Flow<Boolean>
): Flow<Boolean> = combine(
    value.distinctUntilChanged(),
    isFocused.distinctUntilChanged(),
    isValid.distinctUntilChanged()
) { value, isFocused, isValid ->
    value.isNotEmpty() && !isFocused && !isValid
}