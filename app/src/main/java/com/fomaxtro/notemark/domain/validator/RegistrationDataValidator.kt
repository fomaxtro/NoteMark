package com.fomaxtro.notemark.domain.validator

import com.fomaxtro.notemark.domain.util.Error
import com.fomaxtro.notemark.domain.util.ValidationResult

enum class RegistrationDataError : Error {
    USERNAME_TOO_SHORT,
    USERNAME_TOO_LONG,
    EMAIL_INVALID,
    PASSWORD_INVALID,
    PASSWORD_NOT_MATCH
}

class RegistrationDataValidator(
    private val patternMatching: PatternMatching
) {
    fun validateUsername(username: String): ValidationResult<RegistrationDataError> {
        return when {
            username.length < 3 -> ValidationResult.Error(RegistrationDataError.USERNAME_TOO_SHORT)
            username.length > 20  -> ValidationResult.Error(RegistrationDataError.USERNAME_TOO_LONG)
            else -> ValidationResult.Success
        }
    }

    fun validateEmail(email: String): ValidationResult<RegistrationDataError> {
        return if (patternMatching.isEmail(email)) {
            ValidationResult.Success
        } else {
            ValidationResult.Error(RegistrationDataError.EMAIL_INVALID)
        }
    }

    fun validatePassword(password: String): ValidationResult<RegistrationDataError> {
        val isValidLength = password.length >= 8
        val hasSymbol = password.contains(Regex("[^a-zA-Z0-9]"))
        val hasNumber = password.contains(Regex("[0-9]"))

        return if (isValidLength && hasSymbol && hasNumber) {
            ValidationResult.Success
        } else {
            ValidationResult.Error(RegistrationDataError.PASSWORD_INVALID)
        }
    }

    fun validatePasswordConfirmation(
        password: String,
        passwordConfirmation: String
    ): ValidationResult<RegistrationDataError> {
        return if (password == passwordConfirmation) {
            ValidationResult.Success
        } else {
            ValidationResult.Error(RegistrationDataError.PASSWORD_NOT_MATCH)
        }
    }
}