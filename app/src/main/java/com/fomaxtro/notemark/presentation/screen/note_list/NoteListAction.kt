package com.fomaxtro.notemark.presentation.screen.note_list

import java.util.UUID

sealed interface NoteListAction {
    data object OnNewNoteClick : NoteListAction
    data class OnNoteClick(val noteId: UUID) : NoteListAction
}