package com.fomaxtro.notemark.data.datastore.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthInfo(
    val id: String,
    val username: String,
    val tokenPair: TokenPair
)
