package com.fomaxtro.notemark.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.notemark.domain.repository.AuthRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow

class NavigationViewModel(
    authRepository: AuthRepository
) : ViewModel() {
    private val eventChannel = Channel<NavigationEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        authRepository
            .isAuthenticated()
            .drop(1)
            .filter { !it }
            .onEach {
                eventChannel.send(NavigationEvent.Logout)
            }
            .launchIn(viewModelScope)
    }
}