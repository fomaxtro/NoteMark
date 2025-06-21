package com.fomaxtro.notemark.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequest(
    val refreshToken: String
)
