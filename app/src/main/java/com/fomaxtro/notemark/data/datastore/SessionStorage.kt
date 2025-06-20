package com.fomaxtro.notemark.data.datastore

import com.fomaxtro.notemark.data.datastore.dto.AuthInfo

interface SessionStorage {
    suspend fun saveAuthInfo(authInfo: AuthInfo?)
    suspend fun getAuthInfo(): AuthInfo?
}