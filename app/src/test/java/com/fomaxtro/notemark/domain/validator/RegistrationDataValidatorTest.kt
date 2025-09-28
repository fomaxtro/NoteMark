package com.fomaxtro.notemark.domain.validator

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.fail
import com.fomaxtro.notemark.domain.util.ValidationResult
import com.fomaxtro.notemark.fake.FakePatternMatching
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource

class RegistrationDataValidatorTest {
    companion object {
        @JvmStatic
        fun usernameTestCases() = listOf(
            Arguments.of("us", RegistrationDataError.USERNAME_TOO_SHORT),
            Arguments.of((1..21).joinToString { "a" }, RegistrationDataError.USERNAME_TOO_LONG)
        )
    }

    private lateinit var patternMatching: FakePatternMatching
    private lateinit var validator: RegistrationDataValidator

    @BeforeEach
    fun setUp() {
        patternMatching = FakePatternMatching()
        validator = RegistrationDataValidator(patternMatching)
    }

    @Test
    fun `Test validate username - expect success`() {
        val result = validator.validateUsername("Foobar")

        assertThat(result).isInstanceOf<ValidationResult.Success>()
    }

    @ParameterizedTest
    @MethodSource("usernameTestCases")
    fun `Test validate username - expect error`(
        username: String,
        expectedError: RegistrationDataError
    ) {
        when (val result = validator.validateUsername(username)) {
            is ValidationResult.Error -> {
                assertThat(result.error).isEqualTo(expectedError)
            }

            ValidationResult.Success -> {
                fail("Expected error but got success")
            }
        }
    }

    @Test
    fun `Test validate email - expect success`() {
        patternMatching.isEmailValid = true
        val result = validator.validateEmail("test@example.com")

        assertThat(result).isInstanceOf<ValidationResult.Success>()
    }

    @Test
    fun `Test validate email - expect error`() {
        patternMatching.isEmailValid = false

        when (val result = validator.validateEmail("invalid-email")) {
            is ValidationResult.Error -> {
                assertThat(result.error).isEqualTo(RegistrationDataError.EMAIL_INVALID)
            }

            ValidationResult.Success -> {
                fail("Expected error but got success")
            }
        }
    }

    @Test
    fun `Test validate password - expect success`() {
        val result = validator.validatePassword("ValidPass123!")

        assertThat(result).isInstanceOf<ValidationResult.Success>()
    }

    @ParameterizedTest
    @ValueSource(strings = ["short", "rightpassword2", "rightpassword."])
    fun `Test validate password - expect error`(password: String) {
        when (val result = validator.validatePassword(password)) {
            is ValidationResult.Error -> {
                assertThat(result.error).isEqualTo(RegistrationDataError.PASSWORD_INVALID)
            }

            ValidationResult.Success -> {
                fail("Expected error but got success")
            }
        }
    }

    @Test
    fun `Test validate password confirmation - expect success`() {
        val password = "ValidPass123!"
        val result = validator.validatePasswordConfirmation(password, password)

        assertThat(result).isInstanceOf<ValidationResult.Success>()
    }

    @Test
    fun `Test validate password confirmation - expect error`() {
        when (val result = validator.validatePasswordConfirmation("password1", "password2")) {
            is ValidationResult.Error -> {
                assertThat(result.error).isEqualTo(RegistrationDataError.PASSWORD_NOT_MATCH)
            }

            ValidationResult.Success -> {
                fail("Expected error but got success")
            }
        }
    }
}