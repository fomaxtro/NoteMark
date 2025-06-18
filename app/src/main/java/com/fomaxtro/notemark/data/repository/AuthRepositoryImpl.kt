package com.fomaxtro.notemark.data.repository

import com.fomaxtro.notemark.data.mapper.toLoginError
import com.fomaxtro.notemark.data.mapper.toRegisterError
import com.fomaxtro.notemark.data.remote.datasource.AuthDataSource
import com.fomaxtro.notemark.data.remote.dto.LoginRequest
import com.fomaxtro.notemark.data.remote.dto.RegisterRequest
import com.fomaxtro.notemark.domain.error.LoginError
import com.fomaxtro.notemark.domain.error.RegisterError
import com.fomaxtro.notemark.domain.repository.AuthRepository
import com.fomaxtro.notemark.domain.util.EmptyResult
import com.fomaxtro.notemark.domain.util.asEmptyResult
import com.fomaxtro.notemark.domain.util.mapError

class AuthRepositoryImpl(
    private val authDataSource: AuthDataSource
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
        return authDataSource
            .login(
                LoginRequest(
                    email = email,
                    password = password
                )
            )
            .mapError { it.toLoginError() }
            .asEmptyResult()
    }
}