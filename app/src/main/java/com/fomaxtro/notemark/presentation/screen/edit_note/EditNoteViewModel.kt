package com.fomaxtro.notemark.presentation.screen.edit_note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.notemark.domain.model.Note
import com.fomaxtro.notemark.domain.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.Instant
import java.util.UUID

class EditNoteViewModel(
    id: String?,
    defaultTitle: String,
    private val noteRepository: NoteRepository
) : ViewModel() {
    private val _state = MutableStateFlow(
        EditNoteState(
            title = defaultTitle
        )
    )
    val state = _state
        .onStart {
            if (id == null) {
                createNote()
            } else {
                loadNote(id)
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            EditNoteState(
                title = defaultTitle
            )
        )

    private suspend fun createNote() {
        val now = Instant.now()
        val id = UUID.randomUUID()

        noteRepository.createNote(
            Note(
                id = id,
                title = "",
                content = "",
                createdAt = now,
                lastEditedAt = now
            )
        )

        _state.update { it.copy(
            id = id
        ) }
    }

    private suspend fun loadNote(id: String) {

    }

    fun onAction(action: EditNoteAction) {

    }
}