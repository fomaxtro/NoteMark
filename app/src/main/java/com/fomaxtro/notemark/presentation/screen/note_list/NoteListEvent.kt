package com.fomaxtro.notemark.presentation.screen.note_list

import com.fomaxtro.notemark.presentation.ui.UiText
import java.util.UUID

sealed interface NoteListEvent {
    data class NavigateToEditNote(val id: UUID) : NoteListEvent
    data class ShowSystemMessage(val message: UiText) : NoteListEvent
}