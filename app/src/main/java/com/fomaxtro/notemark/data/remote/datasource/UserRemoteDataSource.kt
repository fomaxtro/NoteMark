package com.fomaxtro.notemark.data.remote.datasource

import com.fomaxtro.notemark.data.remote.dto.RegisterRequest
import com.fomaxtro.notemark.data.remote.util.NetworkError
import com.fomaxtro.notemark.data.remote.util.createRoute
import com.fomaxtro.notemark.data.remote.util.safeRemoteCall
import com.fomaxtro.notemark.domain.util.EmptyResult
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class UserRemoteDataSource(
    private val httpClient: HttpClient
) {
    suspend fun register(request: RegisterRequest): EmptyResult<NetworkError> {
        return safeRemoteCall {
            httpClient.post(createRoute("/auth/register")) {
                setBody(request)
            }
        }
    }
}