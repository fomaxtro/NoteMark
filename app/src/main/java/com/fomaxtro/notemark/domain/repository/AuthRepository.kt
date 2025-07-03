package com.fomaxtro.notemark.domain.repository

import com.fomaxtro.notemark.domain.error.LoginError
import com.fomaxtro.notemark.domain.util.EmptyResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String) : EmptyResult<LoginError>
    fun isAuthenticated(): Flow<Boolean>
    suspend fun logout()
}
