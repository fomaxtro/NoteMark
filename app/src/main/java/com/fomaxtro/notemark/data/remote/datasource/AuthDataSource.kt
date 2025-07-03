package com.fomaxtro.notemark.data.remote.datasource

import com.fomaxtro.notemark.data.remote.dto.LoginRequest
import com.fomaxtro.notemark.data.remote.dto.LoginResponse
import com.fomaxtro.notemark.data.remote.dto.LogoutRequest
import com.fomaxtro.notemark.data.remote.util.NetworkError
import com.fomaxtro.notemark.domain.util.EmptyResult
import com.fomaxtro.notemark.domain.util.Result

interface AuthDataSource {
    suspend fun login(request: LoginRequest): Result<LoginResponse, NetworkError>
    suspend fun logout(request: LogoutRequest): EmptyResult<NetworkError>
}