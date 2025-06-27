package com.fomaxtro.notemark.data.repository

import com.fomaxtro.notemark.data.datastore.SecureSessionStorage
import com.fomaxtro.notemark.data.mapper.toRegisterError
import com.fomaxtro.notemark.data.remote.datasource.UserDataSource
import com.fomaxtro.notemark.data.remote.dto.RegisterRequest
import com.fomaxtro.notemark.domain.error.RegisterError
import com.fomaxtro.notemark.domain.repository.UserRepository
import com.fomaxtro.notemark.domain.util.EmptyResult
import com.fomaxtro.notemark.domain.util.mapError
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class UserRepositoryImpl(
    private val userDataSource: UserDataSource,
    private val sessionStorage: SecureSessionStorage
) : UserRepository {
    override suspend fun register(
        username: String,
        email: String,
        password: String
    ): EmptyResult<RegisterError> {
        return userDataSource
            .register(
                RegisterRequest(
                    username = username,
                    email = email,
                    password = password
                )
            )
            .mapError { it.toRegisterError() }
    }

    override suspend fun getUsername(): String? {
        return sessionStorage
            .getAuthInfo()
            .map { it?.username }
            .first()
    }
}