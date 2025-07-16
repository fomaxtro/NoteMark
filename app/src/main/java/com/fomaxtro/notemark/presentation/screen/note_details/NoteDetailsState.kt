package com.fomaxtro.notemark.presentation.screen.note_details

import com.fomaxtro.notemark.presentation.screen.note_details.model.NoteDetailUi

data class NoteDetailsState(
    val note: NoteDetailUi? = null,
    val readerMode: Boolean = false,
    val showControls: Boolean = true
)