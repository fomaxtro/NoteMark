package com.fomaxtro.notemark.presentation.screen.landing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LandingViewModel : ViewModel() {
    private val eventChannel = Channel<LandingEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: LandingAction) {
        when (action) {
            LandingAction.OnGetStartedButtonClick -> onGetStartedButtonClick()
            LandingAction.OnLogInButtonClick -> onLogInButtonClick()
        }
    }

    private fun onLogInButtonClick() {
        viewModelScope.launch {
            eventChannel.send(LandingEvent.NavigateToLogin)
        }
    }

    private fun onGetStartedButtonClick() {
        viewModelScope.launch {
            eventChannel.send(LandingEvent.NavigateToRegistration)
        }
    }
}