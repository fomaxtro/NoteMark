package com.fomaxtro.notemark.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.notemark.data.datastore.SecureSessionStorage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow

class NavigationViewModel(
    sessionStorage: SecureSessionStorage
) : ViewModel() {
    private val eventChannel = Channel<NavigationEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        sessionStorage
            .getAuthInfo()
            .drop(1)
            .filter { it == null }
            .onEach {
                eventChannel.send(NavigationEvent.Logout)
            }
            .launchIn(viewModelScope)
    }
}