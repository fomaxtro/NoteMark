package com.fomaxtro.notemark.domain.validator

import com.fomaxtro.notemark.domain.util.ValidationError
import com.fomaxtro.notemark.domain.util.ValidationResult

class LoginDataValidator(
    private val patternMatching: PatternMatching
) {
    fun validateEmail(email: String): ValidationResult<LoginDataError> {
        return if (patternMatching.isEmail(email)) {
            ValidationResult.Success
        } else {
            ValidationResult.Error(LoginDataError.EMAIL_INVALID)
        }
    }
}

enum class LoginDataError : ValidationError {
    EMAIL_INVALID
}