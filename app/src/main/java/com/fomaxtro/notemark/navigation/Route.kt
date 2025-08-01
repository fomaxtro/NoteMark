package com.fomaxtro.notemark.navigation

import com.fomaxtro.notemark.presentation.screen.edit_note.mode.EditNoteMode
import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data class EditNote(val id: String, val mode: EditNoteMode) : Route
    @Serializable
    data object Landing : Route
    @Serializable
    data object Login : Route
    @Serializable
    data object NoteList : Route
    @Serializable
    data object Registration : Route
    @Serializable
    data object Settings : Route
    @Serializable
    data class NoteDetails(val id: String) : Route
}