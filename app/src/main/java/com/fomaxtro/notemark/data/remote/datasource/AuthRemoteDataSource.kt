package com.fomaxtro.notemark.data.remote.datasource

import com.fomaxtro.notemark.data.remote.dto.LoginRequest
import com.fomaxtro.notemark.data.remote.dto.LoginResponse
import com.fomaxtro.notemark.data.remote.dto.LogoutRequest
import com.fomaxtro.notemark.data.remote.util.NetworkError
import com.fomaxtro.notemark.data.remote.util.createRoute
import com.fomaxtro.notemark.data.remote.util.safeRemoteCall
import com.fomaxtro.notemark.domain.util.EmptyResult
import com.fomaxtro.notemark.domain.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class AuthRemoteDataSource(
    private val httpClient: HttpClient
) {
    suspend fun login(request: LoginRequest): Result<LoginResponse, NetworkError> {
        return safeRemoteCall {
            httpClient.post(createRoute("/auth/login")) {
                setBody(request)
            }
        }
    }

    suspend fun logout(request: LogoutRequest): EmptyResult<NetworkError> {
        return safeRemoteCall {
            httpClient.post(createRoute("/auth/logout")) {
                setBody(request)
            }
        }
    }
}