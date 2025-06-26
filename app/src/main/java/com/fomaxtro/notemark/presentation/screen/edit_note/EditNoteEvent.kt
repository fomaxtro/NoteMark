package com.fomaxtro.notemark.presentation.screen.edit_note

sealed interface EditNoteEvent {
    data object NavigateBack : EditNoteEvent
}