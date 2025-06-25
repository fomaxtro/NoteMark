package com.fomaxtro.notemark.data.remote.datasource

import com.fomaxtro.notemark.data.remote.dto.RegisterRequest
import com.fomaxtro.notemark.data.remote.util.NetworkError
import com.fomaxtro.notemark.domain.util.EmptyResult

interface UserDataSource {
    suspend fun register(request: RegisterRequest): EmptyResult<NetworkError>
}