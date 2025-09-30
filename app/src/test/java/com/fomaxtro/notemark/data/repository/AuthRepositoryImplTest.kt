package com.fomaxtro.notemark.data.repository

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.fail
import com.fomaxtro.notemark.data.datastore.SecureSessionStorage
import com.fomaxtro.notemark.data.remote.datasource.AuthRemoteDataSource
import com.fomaxtro.notemark.data.remote.dto.LoginResponse
import com.fomaxtro.notemark.data.remote.util.NetworkError
import com.fomaxtro.notemark.domain.error.LoginError
import com.fomaxtro.notemark.domain.repository.AuthRepository
import com.fomaxtro.notemark.domain.util.Result
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class AuthRepositoryImplTest {
    private lateinit var authRemoteDataSource: AuthRemoteDataSource
    private lateinit var sessionStorage: SecureSessionStorage
    private lateinit var authRepository: AuthRepository

    @BeforeEach
    fun setUp() {
        authRemoteDataSource = mockk()
        sessionStorage = mockk()
        authRepository = AuthRepositoryImpl(
            authDataSource = authRemoteDataSource,
            sessionStorage = sessionStorage
        )
    }

    @Test
    fun `Test login - expect login successful`() = runTest {
        val loginResponse = LoginResponse(
            accessToken = "access_token",
            refreshToken = "refresh_token",
            username = "username"
        )

        coEvery { authRemoteDataSource.login(any()) } returns Result.Success(loginResponse)
        coJustRun { sessionStorage.saveAuthInfo(any()) }

        val result = authRepository.login("email", "password")

        coVerify { sessionStorage.saveAuthInfo(any()) }

        assertThat(result).isInstanceOf<Result.Success<Unit>>()
    }

    @ParameterizedTest
    @MethodSource("networkErrorTest")
    fun `Test login - expect login failed`(
        actualNetworkError: NetworkError,
        expectedLoginError: LoginError
    ) = runTest {
        coEvery { authRemoteDataSource.login(any()) } returns Result.Error(actualNetworkError)

        when (val result = authRepository.login("email", "password")) {
            is Result.Error -> {
                assertThat(result.error).isEqualTo(expectedLoginError)
            }

            is Result.Success -> {
                fail("Login should have failed")
            }
        }
    }

    companion object {
        @JvmStatic
        fun networkErrorTest() = listOf(
            Arguments.of(NetworkError.TOO_MANY_REQUEST, LoginError.TOO_MANY_ATTEMPTS),
            Arguments.of(NetworkError.SERVER_ERROR, LoginError.SERVICE_UNAVAILABLE),
            Arguments.of(NetworkError.UNAUTHORIZED, LoginError.INVALID_CREDENTIALS),
            Arguments.of(NetworkError.NO_INTERNET, LoginError.NO_INTERNET),
            Arguments.of(NetworkError.UNKNOWN, LoginError.UNKNOWN),
            Arguments.of(NetworkError.SERIALIZATION, LoginError.UNKNOWN),
            Arguments.of(NetworkError.NOT_FOUND, LoginError.UNKNOWN),
            Arguments.of(NetworkError.CONFLICT, LoginError.UNKNOWN),
            Arguments.of(NetworkError.BAD_REQUEST, LoginError.UNKNOWN)
        )
    }
}