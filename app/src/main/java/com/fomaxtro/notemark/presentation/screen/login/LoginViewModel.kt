package com.fomaxtro.notemark.presentation.screen.login

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.notemark.domain.repository.AuthRepository
import com.fomaxtro.notemark.domain.util.Result
import com.fomaxtro.notemark.domain.util.ValidationResult
import com.fomaxtro.notemark.domain.validator.LoginDataValidator
import com.fomaxtro.notemark.presentation.mapper.toUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginDataValidator: LoginDataValidator,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state
        .onStart {
            canLoginEvent()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            LoginState()
        )

    private val eventChannel = Channel<LoginEvent>()
    val events = eventChannel.receiveAsFlow()

    private val validateEmail = state
        .distinctUntilChangedBy { it.email }
        .map { state ->
            loginDataValidator.validateEmail(state.email) is ValidationResult.Success
        }

    private val passwordFlow = snapshotFlow { state.value.password.text.toString() }

    private fun canLoginEvent() {
        combine(passwordFlow, validateEmail) { password, isValidEmail ->
            _state.update {
                it.copy(
                    canLogin = isValidEmail && password.isNotEmpty()
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onAction(action: LoginAction) {
        when (action) {
            LoginAction.OnDontHaveAccountClick -> onDontHaveAccountClick()
            is LoginAction.OnEmailChange -> onEmailChange(action.email)
            LoginAction.OnLogInClick -> onLogInClick()
        }
    }

    private fun onDontHaveAccountClick() {
        viewModelScope.launch {
            eventChannel.send(LoginEvent.NavigateToRegistration)
        }
    }

    private fun onLogInClick() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }

            val result = with(state.value) {
                authRepository.login(email, password.text.toString())
            }

            _state.update {
                it.copy(
                    isLoading = false
                )
            }

            when (result) {
                is Result.Error -> {
                    eventChannel.send(LoginEvent.ShowMessage(result.error.toUiText()))
                }

                is Result.Success -> {
                    eventChannel.send(LoginEvent.NavigateToNoteList)
                }
            }
        }
    }

    private fun onEmailChange(email: String) {
        _state.update {
            it.copy(
                email = email
            )
        }
    }
}