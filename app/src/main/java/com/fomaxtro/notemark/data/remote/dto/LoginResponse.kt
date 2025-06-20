package com.fomaxtro.notemark.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val username: String
)
