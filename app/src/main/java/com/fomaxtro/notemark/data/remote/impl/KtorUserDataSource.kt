package com.fomaxtro.notemark.data.remote.impl

import com.fomaxtro.notemark.data.remote.datasource.UserDataSource
import com.fomaxtro.notemark.data.remote.dto.RegisterRequest
import com.fomaxtro.notemark.data.remote.util.NetworkError
import com.fomaxtro.notemark.data.remote.util.createRoute
import com.fomaxtro.notemark.data.remote.util.safeRemoteCall
import com.fomaxtro.notemark.domain.util.EmptyResult
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class KtorUserDataSource(
    private val httpClient: HttpClient
) : UserDataSource {
    override suspend fun register(request: RegisterRequest): EmptyResult<NetworkError> {
        return safeRemoteCall {
            httpClient.post(createRoute("/auth/register")) {
                setBody(request)
            }
        }
    }
}