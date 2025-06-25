package com.fomaxtro.notemark.domain.repository

import com.fomaxtro.notemark.domain.error.RegisterError
import com.fomaxtro.notemark.domain.util.EmptyResult

interface UserRepository {
    suspend fun register(
        username: String,
        email: String,
        password: String
    ): EmptyResult<RegisterError>
}