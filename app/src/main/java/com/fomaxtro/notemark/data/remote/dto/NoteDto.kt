package com.fomaxtro.notemark.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class NoteDto(
    val id: String,
    val title: String,
    val content: String,
    val createdAt: String,
    val lastEditedAt: String
)
