package com.fomaxtro.notemark.presentation.screen.note_list.model

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class NoteUi(
    val id: Uuid,
    val title: String,
    val content: String,
    val createdAt: String
)
