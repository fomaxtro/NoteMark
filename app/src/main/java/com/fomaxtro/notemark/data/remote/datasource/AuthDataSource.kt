package com.fomaxtro.notemark.data.remote.datasource

import com.fomaxtro.notemark.data.remote.dto.LoginRequest
import com.fomaxtro.notemark.data.remote.dto.RegisterRequest
import com.fomaxtro.notemark.data.remote.dto.TokenResponse
import com.fomaxtro.notemark.data.remote.util.NetworkError
import com.fomaxtro.notemark.domain.util.EmptyResult
import com.fomaxtro.notemark.domain.util.Result

interface AuthDataSource {
    suspend fun register(request: RegisterRequest): EmptyResult<NetworkError>
    suspend fun login(request: LoginRequest): Result<TokenResponse, NetworkError>
}