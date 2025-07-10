package com.fomaxtro.notemark.presentation.screen.note_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.notemark.domain.repository.NoteRepository
import com.fomaxtro.notemark.presentation.mapper.toNoteDetailUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class NoteDetailsViewModel(
    id: String,
    private val noteRepository: NoteRepository
) : ViewModel() {
    private val _state = MutableStateFlow(NoteDetailsState())
    val state = _state
        .onStart {
            loadNote(UUID.fromString(id))
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            NoteDetailsState()
        )

    private val eventChannel = Channel<NoteDetailsEvent>()
    val events = eventChannel.receiveAsFlow()

    private fun loadNote(id: UUID) {
        noteRepository
            .findById(id)
            .onEach { note ->
                _state.update {
                    it.copy(
                        note = note.toNoteDetailUi()
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: NoteDetailsAction) {
        when (action) {
            NoteDetailsAction.OnNavigateBackClick -> onNavigateBackClick()
            NoteDetailsAction.OnEditNoteClick -> onEditNoteClick()
        }
    }

    private fun onEditNoteClick() {
        viewModelScope.launch {
            eventChannel.send(NoteDetailsEvent.NavigateToEditNote)
        }
    }

    private fun onNavigateBackClick() {
        viewModelScope.launch {
            eventChannel.send(NoteDetailsEvent.NavigateBack)
        }
    }
}