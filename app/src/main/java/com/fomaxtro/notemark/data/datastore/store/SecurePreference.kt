package com.fomaxtro.notemark.data.datastore.store

import com.fomaxtro.notemark.data.datastore.dto.TokenPair
import kotlinx.serialization.Serializable

@Serializable
data class SecurePreference(
    val tokenPair: TokenPair? = null
)
