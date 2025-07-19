package com.fomaxtro.notemark.data.datastore

import com.fomaxtro.notemark.data.datastore.dto.AuthInfo
import com.fomaxtro.notemark.data.datastore.dto.TokenPair
import kotlinx.coroutines.flow.Flow

interface SessionStorage {
    suspend fun saveAuthInfo(authInfo: AuthInfo)
    fun getAuthInfo(): Flow<AuthInfo?>
    suspend fun removeAuthInfo()
    suspend fun saveTokenPair(tokenPair: TokenPair)
    suspend fun getTokenPair(): TokenPair?
    suspend fun getUserId(): String?
}