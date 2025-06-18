package com.fomaxtro.notemark.domain.repository

import com.fomaxtro.notemark.domain.error.LoginError
import com.fomaxtro.notemark.domain.error.RegisterError
import com.fomaxtro.notemark.domain.util.EmptyResult

interface AuthRepository {
    suspend fun register(
        username: String,
        email: String,
        password: String
    ): EmptyResult<RegisterError>
    suspend fun login(email: String, password: String) : EmptyResult<LoginError>
}
