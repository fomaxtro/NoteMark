package com.fomaxtro.notemark.presentation.screen.note_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.notemark.domain.model.Note
import com.fomaxtro.notemark.domain.repository.NoteRepository
import com.fomaxtro.notemark.domain.repository.UserRepository
import com.fomaxtro.notemark.domain.util.Result
import com.fomaxtro.notemark.presentation.mapper.toNoteUi
import com.fomaxtro.notemark.presentation.mapper.toUiText
import com.fomaxtro.notemark.presentation.model.NoteUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID

class NoteListViewModel(
    private val defaultTitle: String,
    private val noteRepository: NoteRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _state = MutableStateFlow(NoteListState())
    val state = _state
        .onStart {
            getUsername()
            loadNotes()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            NoteListState()
        )

    private val eventChannel = Channel<NoteListEvent>()
    val events = eventChannel.receiveAsFlow()

    private var selectedNote: NoteUi? = null

    private fun getUsername() {
        viewModelScope.launch {
            val username = userRepository.getUsername()

            _state.update {
                it.copy(
                    username = username ?: "NA"
                )
            }
        }
    }

    private fun loadNotes() {
        noteRepository
            .getRecentNotes()
            .map { notes ->
                notes.map { it.toNoteUi() }
            }
            .onEach { notes ->
                _state.update {
                    it.copy(
                        notes = notes
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: NoteListAction) {
        when (action) {
            NoteListAction.OnNewNoteClick -> onNewNoteClick()
            is NoteListAction.OnNoteClick -> onNoteClick(action.note)
            NoteListAction.OnDeleteNoteDialogConfirm -> onDeleteNoteDialogConfirm()
            NoteListAction.OnDeleteNoteDialogDismiss -> onDeleteNoteDialogDismiss()
            is NoteListAction.OnNoteLongClick -> onNoteLongClick(action.note)
            NoteListAction.OnSettingsClick -> onSettingsClick()
        }
    }

    private fun onSettingsClick() {
        viewModelScope.launch {
            eventChannel.send(NoteListEvent.NavigateToSettings)
        }
    }

    private fun onNoteLongClick(note: NoteUi) {
        selectedNote = note

        _state.update {
            it.copy(
                showDeleteNoteDialog = true
            )
        }
    }

    private fun onDeleteNoteDialogDismiss() {
        selectedNote = null

        _state.update {
            it.copy(
                showDeleteNoteDialog = false
            )
        }
    }

    private fun onDeleteNoteDialogConfirm() {
        viewModelScope.launch {
            selectedNote?.let { note ->
                noteRepository.delete(note.id)
            }

            _state.update {
                it.copy(
                    showDeleteNoteDialog = false
                )
            }
        }
    }

    private fun onNoteClick(note: NoteUi) {
        viewModelScope.launch {
            eventChannel.send(
                NoteListEvent.NavigateToEditNote(note.id)
            )
        }
    }

    private fun onNewNoteClick() {
        viewModelScope.launch {
            val now = Instant.now()

            val note = Note(
                id = UUID.randomUUID(),
                title = defaultTitle,
                content = "",
                createdAt = now,
                lastEditedAt = now
            )

            when (val result = noteRepository.create(note)) {
                is Result.Error -> {
                    eventChannel.send(
                        NoteListEvent.ShowSystemMessage(result.error.toUiText())
                    )
                }
                is Result.Success -> {
                    eventChannel.send(
                        NoteListEvent.NavigateToEditNote(note.id)
                    )
                }
            }
        }
    }
}