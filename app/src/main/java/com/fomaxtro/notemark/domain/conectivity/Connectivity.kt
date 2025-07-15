package com.fomaxtro.notemark.domain.conectivity

import kotlinx.coroutines.flow.Flow

interface Connectivity {
    fun hasInternetConnection(): Flow<Boolean>
}