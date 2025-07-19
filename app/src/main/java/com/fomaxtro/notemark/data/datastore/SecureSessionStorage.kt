package com.fomaxtro.notemark.data.datastore

import androidx.datastore.core.DataStore
import com.fomaxtro.notemark.data.datastore.dto.AuthInfo
import com.fomaxtro.notemark.data.datastore.dto.TokenPair
import com.fomaxtro.notemark.data.datastore.store.SecurePreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SecureSessionStorage(
    private val dataStore: DataStore<SecurePreference>
) {
    suspend fun saveAuthInfo(authInfo: AuthInfo) {
        dataStore.updateData {
            it.copy(
                authInfo = authInfo
            )
        }
    }

    fun getAuthInfo(): Flow<AuthInfo?> {
        return dataStore
            .data
            .map { it.authInfo }
    }

    suspend fun removeAuthInfo() {
        dataStore.updateData {
            it.copy(
                authInfo = null
            )
        }
    }

    suspend fun saveTokenPair(tokenPair: TokenPair) {
        dataStore.updateData {
            it.copy(
                authInfo = it.authInfo?.copy(
                    tokenPair = tokenPair
                )
            )
        }
    }

    suspend fun getTokenPair(): TokenPair? {
        return dataStore
            .data
            .map { it.authInfo?.tokenPair }
            .first()
    }

    suspend fun getUserId(): String? {
        return dataStore
            .data
            .map { it.authInfo?.id }
            .first()
    }
}