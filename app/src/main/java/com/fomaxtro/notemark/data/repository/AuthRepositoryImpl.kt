package com.fomaxtro.notemark.data.repository

import com.fomaxtro.notemark.data.datastore.SecureSessionStorage
import com.fomaxtro.notemark.data.datastore.SessionStorage
import com.fomaxtro.notemark.data.datastore.dto.AuthInfo
import com.fomaxtro.notemark.data.datastore.dto.TokenPair
import com.fomaxtro.notemark.data.mapper.toLoginError
import com.fomaxtro.notemark.data.mapper.toRegisterError
import com.fomaxtro.notemark.data.remote.datasource.AuthDataSource
import com.fomaxtro.notemark.data.remote.dto.LoginRequest
import com.fomaxtro.notemark.data.remote.dto.RegisterRequest
import com.fomaxtro.notemark.domain.error.LoginError
import com.fomaxtro.notemark.domain.error.RegisterError
import com.fomaxtro.notemark.domain.repository.AuthRepository
import com.fomaxtro.notemark.domain.util.EmptyResult
import com.fomaxtro.notemark.domain.util.Result
import com.fomaxtro.notemark.domain.util.asEmptyResult
import com.fomaxtro.notemark.domain.util.mapError

class AuthRepositoryImpl(
    private val authDataSource: AuthDataSource,
    private val secureSessionStorage: SecureSessionStorage,
    private val sessionStorage: SessionStorage
) : AuthRepository {
    override suspend fun register(
        username: String,
        email: String,
        password: String
    ): EmptyResult<RegisterError> {
        return authDataSource
            .register(
                RegisterRequest(
                    username = username,
                    email = email,
                    password = password
                )
            )
            .mapError { it.toRegisterError() }
    }

    override suspend fun login(email: String, password: String): EmptyResult<LoginError> {
        val result =  authDataSource
            .login(
                LoginRequest(
                    email = email,
                    password = password
                )
            )

        return when (result) {
            is Result.Error -> Result.Error(result.error.toLoginError())
            is Result.Success -> {
                with(result.data) {
                    secureSessionStorage.saveTokenPair(
                        TokenPair(
                            accessToken = accessToken,
                            refreshToken = refreshToken
                        )
                    )

                    sessionStorage.saveAuthInfo(
                        AuthInfo(
                            username = username
                        )
                    )
                }

                result.asEmptyResult()
            }
        }
    }
}