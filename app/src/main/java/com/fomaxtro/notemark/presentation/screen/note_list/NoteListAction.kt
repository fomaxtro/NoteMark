package com.fomaxtro.notemark.presentation.screen.note_list

sealed interface NoteListAction {
    data object OnNewNoteClick : NoteListAction
}