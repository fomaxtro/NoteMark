package com.fomaxtro.notemark.data.remote.impl

import com.fomaxtro.notemark.data.remote.datasource.AuthDataSource
import com.fomaxtro.notemark.data.remote.dto.LoginRequest
import com.fomaxtro.notemark.data.remote.dto.LoginResponse
import com.fomaxtro.notemark.data.remote.util.NetworkError
import com.fomaxtro.notemark.data.remote.util.createRoute
import com.fomaxtro.notemark.data.remote.util.safeRemoteCall
import com.fomaxtro.notemark.domain.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class KtorAuthDataSource(
    private val httpClient: HttpClient
) : AuthDataSource {
    override suspend fun login(request: LoginRequest): Result<LoginResponse, NetworkError> {
        return safeRemoteCall {
            httpClient.post(createRoute("/auth/login")) {
                setBody(request)
                header("Debug", true)
            }
        }
    }
}