package com.fomaxtro.notemark.data.datastore.dto

import kotlinx.serialization.Serializable

@Serializable
data class TokenPair(
    val accessToken : String,
    val refreshToken : String
)
