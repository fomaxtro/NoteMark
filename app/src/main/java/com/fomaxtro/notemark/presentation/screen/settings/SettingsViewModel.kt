package com.fomaxtro.notemark.presentation.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.notemark.R
import com.fomaxtro.notemark.domain.conectivity.Connectivity
import com.fomaxtro.notemark.domain.model.SyncInterval
import com.fomaxtro.notemark.domain.model.SyncStatus
import com.fomaxtro.notemark.domain.model.UserPreferences
import com.fomaxtro.notemark.domain.repository.SyncRepository
import com.fomaxtro.notemark.domain.repository.UserPreferencesRepository
import com.fomaxtro.notemark.domain.use_case.Logout
import com.fomaxtro.notemark.presentation.ui.UiText
import com.fomaxtro.notemark.presentation.util.toSyncDateTimeUiText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
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
    private val applicationScope: CoroutineScope,
    private val syncRepository: SyncRepository,
    private val connectivity: Connectivity,
    private val logout: Logout,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    private val _state = MutableStateFlow(SettingsState())
    val state = _state
        .onStart {
            observeInternetConnection()
            loadUserPreferences()
            observeLastSyncTime()
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

    private suspend fun loadUserPreferences() {
        val userPreferences = userPreferencesRepository.find()

        _state.update {
            it.copy(
                syncInterval = userPreferences.syncInterval
            )
        }
    }

    private fun observeLastSyncTime() {
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
            is SettingsAction.OnSyncIntervalChange -> onSyncIntervalChange(action.interval)
        }
    }

    private fun onSyncIntervalChange(interval: SyncInterval) {
        _state.update { it.copy(
            syncInterval = interval
        ) }

        viewModelScope.launch {
            syncRepository.schedulePeriodicSync(interval)
            userPreferencesRepository.save(
                UserPreferences(
                    syncInterval = interval
                )
            )
        }
    }

    private fun onSyncDataClick() {
        viewModelScope.launch {
            if (!state.value.hasInternetConnection) {
                eventChannel.send(
                    SettingsEvent.ShoSystemMessage(
                        UiText.StringResource(R.string.no_internet)
                    )
                )

                return@launch
            }

            _state.update {
                it.copy(
                    isSyncing = true
                )
            }

            val syncDeferred = applicationScope.async {
                syncRepository.performFullSync()
            }

            syncDeferred
                .await()
                .collect { syncStatus ->
                    when (syncStatus) {
                        SyncStatus.SYNCED -> {
                            _state.update {
                                it.copy(
                                    isSyncing = false
                                )
                            }

                            eventChannel.send(
                                SettingsEvent.ShoSystemMessage(
                                    UiText.StringResource(R.string.sync_succeeded)
                                )
                            )
                        }

                        SyncStatus.FAILED -> {
                            _state.update {
                                it.copy(
                                    isSyncing = false
                                )
                            }

                            eventChannel.send(
                                SettingsEvent.ShoSystemMessage(
                                    UiText.StringResource(R.string.sync_failed)
                                )
                            )
                        }

                        else -> Unit
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
        viewModelScope.launch {
            if (!state.value.hasInternetConnection) {
                eventChannel.send(
                    SettingsEvent.ShowMessage(
                        UiText.StringResource(
                            R.string.you_need_internet_connection_to_logout
                        )
                    )
                )

                return@launch
            }

            _state.update {
                it.copy(
                    isLoggingOut = true
                )
            }

            applicationScope.launch {
                logout()
            }
        }
    }
}