package com.fomaxtro.notemark.data.datastore

import com.fomaxtro.notemark.data.datastore.dto.AuthInfo
import com.fomaxtro.notemark.data.datastore.dto.TokenPair
import kotlinx.coroutines.flow.Flow

interface SecureSessionStorage {
    suspend fun saveAuthInfo(authInfo: AuthInfo)
    suspend fun getAuthInfo(): Flow<AuthInfo?>
    suspend fun removeAuthInfo()
    suspend fun saveTokenPair(tokenPair: TokenPair)
    suspend fun getTokenPair(): TokenPair?
}