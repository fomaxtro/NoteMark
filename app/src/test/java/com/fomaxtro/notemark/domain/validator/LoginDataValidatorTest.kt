package com.fomaxtro.notemark.domain.validator

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.fail
import com.fomaxtro.notemark.domain.util.ValidationResult
import com.fomaxtro.notemark.fake.FakePatternMatching
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LoginDataValidatorTest {
    private lateinit var patternMatching: FakePatternMatching
    private lateinit var validator: LoginDataValidator

    @BeforeEach
    fun setUp() {
        patternMatching = FakePatternMatching()
        validator = LoginDataValidator(patternMatching)
    }

    @Test
    fun `Test validate email - expect success`() {
        patternMatching.isEmailValid = true

        val result = validator.validateEmail("email@email.com")

        assertThat(result).isInstanceOf<ValidationResult.Success>()
    }

    @Test
    fun `Test validate email - expect error`() {
        patternMatching.isEmailValid = false

        when (val result = validator.validateEmail("invalid")) {
            is ValidationResult.Error -> {
                assertThat(result.error).isEqualTo(LoginDataError.EMAIL_INVALID)
            }

            ValidationResult.Success -> {
                fail("Expected error but got success")
            }
        }
    }
}