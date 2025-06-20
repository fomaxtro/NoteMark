package com.fomaxtro.notemark.data.datastore

import com.fomaxtro.notemark.data.datastore.dto.TokenPair

interface SecureSessionStorage {
    suspend fun saveTokenPair(tokenPair: TokenPair?)
    suspend fun getTokenPair(): TokenPair?
}