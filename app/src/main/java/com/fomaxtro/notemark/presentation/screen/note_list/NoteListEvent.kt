package com.fomaxtro.notemark.presentation.screen.note_list

sealed interface NoteListEvent {
    data object NavigateToEditNote : NoteListEvent
}