package com.fomaxtro.notemark.data.datastore.impl

import androidx.datastore.core.DataStore
import com.fomaxtro.notemark.data.datastore.SecureSessionStorage
import com.fomaxtro.notemark.data.datastore.dto.AuthInfo
import com.fomaxtro.notemark.data.datastore.dto.TokenPair
import com.fomaxtro.notemark.data.datastore.store.SecurePreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DataStoreSecureSessionStorage(
    private val dataStore: DataStore<SecurePreference>
) : SecureSessionStorage {
    override suspend fun saveAuthInfo(authInfo: AuthInfo) {
        dataStore.updateData {
            it.copy(
                authInfo = authInfo
            )
        }
    }

    override suspend fun getAuthInfo(): Flow<AuthInfo?> {
        return dataStore
            .data
            .map { it.authInfo }
    }

    override suspend fun removeAuthInfo() {
        dataStore.updateData {
            it.copy(
                authInfo = null
            )
        }
    }

    override suspend fun saveTokenPair(tokenPair: TokenPair) {
        dataStore.updateData {
            it.copy(
                authInfo = it.authInfo?.copy(
                    tokenPair = tokenPair
                )
            )
        }
    }

    override suspend fun getTokenPair(): TokenPair? {
        return dataStore
            .data
            .map { it.authInfo?.tokenPair }
            .first()
    }
}