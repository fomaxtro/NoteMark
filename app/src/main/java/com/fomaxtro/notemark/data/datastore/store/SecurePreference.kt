package com.fomaxtro.notemark.data.datastore.store

import com.fomaxtro.notemark.data.datastore.dto.AuthInfo
import kotlinx.serialization.Serializable

@Serializable
data class SecurePreference(
    val authInfo: AuthInfo? = null
)
