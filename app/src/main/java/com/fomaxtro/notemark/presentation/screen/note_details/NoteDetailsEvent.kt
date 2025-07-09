package com.fomaxtro.notemark.presentation.screen.note_details

sealed interface NoteDetailsEvent {
    data object NavigateBack : NoteDetailsEvent
}