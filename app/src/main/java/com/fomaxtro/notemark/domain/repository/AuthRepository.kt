package com.fomaxtro.notemark.domain.repository

import com.fomaxtro.notemark.domain.error.LoginError
import com.fomaxtro.notemark.domain.util.EmptyResult

interface AuthRepository {
    suspend fun login(email: String, password: String) : EmptyResult<LoginError>
}
