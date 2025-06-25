package com.fomaxtro.notemark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.notemark.data.datastore.SecureSessionStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class MainViewModel(
    private val sessionStorage: SecureSessionStorage
) : ViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state = _state
        .onStart {
            val authInfo = sessionStorage
                .getAuthInfo()
                .first()

            _state.update { it.copy(
                isLoggedIn = authInfo != null,
                isCheckingAuth = false
            ) }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            MainState()
        )
}