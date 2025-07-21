package com.fomaxtro.notemark.presentation.screen.note_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.notemark.domain.repository.NoteRepository
import com.fomaxtro.notemark.presentation.mapper.toNoteDetailUi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
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
            toggleControls()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            NoteDetailsState()
        )

    private val eventChannel = Channel<NoteDetailsEvent>()
    val events = eventChannel.receiveAsFlow()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun toggleControls() {
        state
            .map { it.readerMode }
            .distinctUntilChanged()
            .flatMapLatest { readerNote ->
                if (readerNote) {
                    state
                        .distinctUntilChangedBy { it.showControls }
                        .filter { it.showControls }
                        .debounce(5000L)
                        .onEach {
                            _state.update {
                                it.copy(
                                    showControls = false
                                )
                            }
                        }
                } else {
                    emptyFlow()
                }
            }
            .launchIn(viewModelScope)
    }

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
            is NoteDetailsAction.OnReaderModeClick -> onReaderModeClick(action.readerMode)
            NoteDetailsAction.OnTapScreen -> onTapScreen()
            NoteDetailsAction.OnContentScroll -> onContentScroll()
        }
    }

    private fun onContentScroll() {
        if (state.value.readerMode) {
            _state.update {
                it.copy(
                    showControls = false
                )
            }
        }
    }

    private fun onTapScreen() {
        if (state.value.readerMode) {
            _state.update {
                it.copy(
                    showControls = !it.showControls
                )
            }
        }
    }

    private fun onReaderModeClick(readerMode: Boolean) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    readerMode = readerMode
                )
            }

            if (readerMode) {
                eventChannel.send(NoteDetailsEvent.SetLandscapeOrientation)
            } else {
                eventChannel.send(NoteDetailsEvent.SetUnspecifiedOrientation)
            }
        }
    }

    private fun onEditNoteClick() {
        viewModelScope.launch {
            eventChannel.send(NoteDetailsEvent.SetUnspecifiedOrientation)
            eventChannel.send(NoteDetailsEvent.NavigateToEditNote)
        }
    }

    private fun onNavigateBackClick() {
        viewModelScope.launch {
            eventChannel.send(NoteDetailsEvent.SetUnspecifiedOrientation)
            eventChannel.send(NoteDetailsEvent.NavigateBack)
        }
    }
}