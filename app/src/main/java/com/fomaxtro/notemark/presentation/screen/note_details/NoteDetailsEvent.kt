package com.fomaxtro.notemark.presentation.screen.note_details

sealed interface NoteDetailsEvent {
    data object NavigateBack : NoteDetailsEvent
    data object NavigateToEditNote : NoteDetailsEvent
    data object SetLandscapeOrientation : NoteDetailsEvent
    data object SetUnspecifiedOrientation : NoteDetailsEvent
}