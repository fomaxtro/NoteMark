package com.fomaxtro.notemark.presentation.screen.note_list

import com.fomaxtro.notemark.presentation.screen.note_list.model.NoteUi

data class NoteListState(
    val notes: List<NoteUi> = emptyList(),
    val username: String = "",
    val showDeleteNoteDialog: Boolean = false,
    val hasInternet: Boolean = true
)
