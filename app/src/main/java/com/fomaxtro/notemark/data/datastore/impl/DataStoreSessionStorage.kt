package com.fomaxtro.notemark.data.datastore.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.fomaxtro.notemark.data.datastore.SessionStorage
import com.fomaxtro.notemark.data.datastore.dto.AuthInfo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class DataStoreSessionStorage(
    private val store: DataStore<Preferences>
) : SessionStorage {
    companion object {
        private val AUTH_INFO = stringPreferencesKey("auth_info")
    }

    override suspend fun saveAuthInfo(authInfo: AuthInfo?) {
        store.edit { preferences ->
            if (authInfo != null) {
                preferences[AUTH_INFO] = Json.encodeToString(authInfo)
            } else {
                preferences.remove(AUTH_INFO)
            }
        }
    }

    override suspend fun getAuthInfo(): AuthInfo? {
        return store
            .data
            .map { preferences ->
                preferences[AUTH_INFO]?.let { authInfo ->
                    Json.decodeFromString<AuthInfo>(authInfo)
                }
            }
            .first()
    }
}