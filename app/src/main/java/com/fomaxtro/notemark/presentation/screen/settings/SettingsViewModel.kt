package com.fomaxtro.notemark.presentation.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.notemark.R
import com.fomaxtro.notemark.domain.conectivity.Connectivity
import com.fomaxtro.notemark.domain.model.SyncStatus
import com.fomaxtro.notemark.domain.repository.AuthRepository
import com.fomaxtro.notemark.domain.repository.SyncRepository
import com.fomaxtro.notemark.presentation.ui.UiText
import com.fomaxtro.notemark.presentation.util.toSyncDateTimeUiText
import kotlinx.coroutines.CoroutineScope
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

class SettingsViewModel(
    private val authRepository: AuthRepository,
    private val applicationScope: CoroutineScope,
    private val syncRepository: SyncRepository,
    private val connectivity: Connectivity
) : ViewModel() {
    private val _state = MutableStateFlow(SettingsState())
    val state = _state
        .onStart {
            observeInternetConnection()
            loadLastSyncStatus()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            SettingsState()
        )

    private val eventChannel = Channel<SettingsEvent>()
    val events = eventChannel.receiveAsFlow()

    private fun observeInternetConnection() {
        connectivity
            .hasInternetConnection()
            .onEach { hasInternetConnection ->
                _state.update {
                    it.copy(
                        hasInternetConnection = hasInternetConnection
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun loadLastSyncStatus() {
        syncRepository
            .getLastSyncTime()
            .onEach { lastSyncTime ->
                _state.update {
                    it.copy(
                        lastSyncTime = lastSyncTime.toSyncDateTimeUiText()
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: SettingsAction) {
        when (action) {
            SettingsAction.OnLogoutClick -> onLogoutClick()
            SettingsAction.OnNavigateBackClick -> onNavigateBackClick()
            SettingsAction.OnSyncDataClick -> onSyncDataClick()
        }
    }

    private fun onSyncDataClick() {
        viewModelScope.launch {
            if (!state.value.hasInternetConnection) {
                eventChannel.send(SettingsEvent.ShoSystemMessage(
                    UiText.StringResource(R.string.no_internet)
                ))

                return@launch
            }

            _state.update {
                it.copy(
                    isSyncing = true
                )
            }

            syncRepository
                .performFullSync()
                .collect { syncStatus ->
                    when (syncStatus) {
                        SyncStatus.SYNCING -> {
                            eventChannel.send(SettingsEvent.ShoSystemMessage(
                                UiText.StringResource(R.string.syncing_data)
                            ))
                        }
                        SyncStatus.SYNCED -> {
                            _state.update {
                                it.copy(
                                    isSyncing = false
                                )
                            }

                            eventChannel.send(SettingsEvent.ShoSystemMessage(
                                UiText.StringResource(R.string.sync_succeeded)
                            ))
                        }
                        SyncStatus.FAILED -> {
                            _state.update {
                                it.copy(
                                    isSyncing = false
                                )
                            }

                            eventChannel.send(SettingsEvent.ShoSystemMessage(
                                UiText.StringResource(R.string.sync_failed)
                            ))
                        }
                    }
                }
        }
    }

    private fun onNavigateBackClick() {
        viewModelScope.launch {
            eventChannel.send(SettingsEvent.NavigateBack)
        }
    }

    private fun onLogoutClick() {
        _state.update {
            it.copy(
                isLoggingOut = true
            )
        }

        applicationScope.launch {
            authRepository.logout()
        }
    }
}