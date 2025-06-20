package com.fomaxtro.notemark.data.datastore.impl

import androidx.datastore.core.DataStore
import com.fomaxtro.notemark.data.datastore.SecureSessionStorage
import com.fomaxtro.notemark.data.datastore.dto.TokenPair
import com.fomaxtro.notemark.data.datastore.store.SecurePreference
import kotlinx.coroutines.flow.first

class DataStoreSecureSessionStorage(
    private val store: DataStore<SecurePreference>
) : SecureSessionStorage {
    override suspend fun saveTokenPair(tokenPair: TokenPair?) {
        store.updateData {
            it.copy(
                tokenPair = tokenPair
            )
        }
    }

    override suspend fun getTokenPair(): TokenPair? {
        return store
            .data
            .first()
            .tokenPair
    }
}