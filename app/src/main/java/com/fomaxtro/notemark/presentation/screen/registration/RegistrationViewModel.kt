package com.fomaxtro.notemark.presentation.screen.registration

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.notemark.domain.repository.UserRepository
import com.fomaxtro.notemark.domain.util.Result
import com.fomaxtro.notemark.domain.validator.RegistrationDataValidator
import com.fomaxtro.notemark.presentation.mapper.toUiText
import com.fomaxtro.notemark.presentation.ui.createFieldValidationFlow
import com.fomaxtro.notemark.presentation.ui.createIsErrorFlow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegistrationViewModel(
    private val validator: RegistrationDataValidator,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _state = MutableStateFlow(RegistrationState())
    val state = _state
        .onStart {
            observeUsername()
            observeEmail()
            observePassword()
            observePasswordConfirmation()
            observeFields()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            RegistrationState()
        )

    private val eventChannel = Channel<RegistrationEvent>()
    val events = eventChannel.receiveAsFlow()

    private val isUsernameValid = createFieldValidationFlow(
        shouldValidate = state.map { it.isUsernameFocused },
        value = state.map { it.username },
        validator = { validator.validateUsername(it) },
        onError = { error ->
            _state.update {
                it.copy(
                    usernameError = error?.toUiText()
                )
            }
        }
    )

    private val isEmailValid = createFieldValidationFlow(
        shouldValidate = state.map { it.isEmailFocused },
        value = state.map { it.email },
        validator = { validator.validateEmail(it) },
        onError = { error ->
            _state.update {
                it.copy(
                    emailError = error?.toUiText()
                )
            }
        }
    )

    private val passwordFlow = snapshotFlow { state.value.password.text.toString() }
    private val isPasswordValid = createFieldValidationFlow(
        shouldValidate = state.map { it.isPasswordFocused },
        value = passwordFlow,
        validator = { validator.validatePassword(it) },
        onError = { error ->
            _state.update {
                it.copy(
                    passwordError = error?.toUiText()
                )
            }
        }
    )

    private val passwordConfirmationFlow = snapshotFlow {
        state.value.passwordConfirmation.text.toString()
    }
    private val isPasswordConfirmationValid = createFieldValidationFlow(
        shouldValidate = state.map { state ->
            state.isPasswordFocused || state.isPasswordConfirmationFocused
        },
        value = combine(passwordFlow, passwordConfirmationFlow) { password, passwordConfirmation ->
            password to passwordConfirmation
        },
        validator = { validator.validatePasswordConfirmation(it.first, it.second) },
        onError = { error ->
            _state.update {
                it.copy(
                    passwordConfirmationError = error?.toUiText()
                )
            }
        }
    )

    private fun observeUsername() {
        createIsErrorFlow(
            value = state.map { it.username },
            isFocused = state.map { it.isUsernameFocused },
            isValid = isUsernameValid
        )
            .onEach { isError ->
                _state.update {
                    it.copy(
                        isUsernameError = isError
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun observeEmail() {
        createIsErrorFlow(
            value = state.map { it.email },
            isFocused = state.map { it.isEmailFocused },
            isValid = isEmailValid
        )
            .onEach { isError ->
                _state.update {
                    it.copy(
                        isEmailError = isError
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun observePassword() {
        createIsErrorFlow(
            value = passwordFlow,
            isFocused = state.map { it.isPasswordFocused },
            isValid = isPasswordValid
        )
            .onEach { isError ->
                _state.update {
                    it.copy(
                        isPasswordError = isError
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun observePasswordConfirmation() {
        createIsErrorFlow(
            value = passwordConfirmationFlow,
            isFocused = state.map { it.isPasswordConfirmationFocused },
            isValid = isPasswordConfirmationValid
        )
            .onEach { isError ->
                _state.update {
                    it.copy(
                        isPasswordConfirmationError = isError
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun observeFields() {
        combine(
            isUsernameValid,
            isEmailValid,
            isPasswordValid,
            isPasswordConfirmationValid
        ) { isValidUsername, isValidEmail, isValidPassword, isValidPasswordConfirmation ->
            _state.update {
                it.copy(
                    canCreateAccount = isValidUsername
                            && isValidEmail
                            && isValidPassword
                            && isValidPasswordConfirmation
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onAction(action: RegistrationAction) {
        when (action) {
            is RegistrationAction.OnUsernameChange -> onUsernameChange(action.username)
            is RegistrationAction.OnUsernameFocusChange -> onUsernameFocusChange(action.isFocused)
            is RegistrationAction.OnEmailChange -> onEmailChange(action.email)
            is RegistrationAction.OnEmailFocusChange -> onEmailFocusChange(action.isFocused)
            is RegistrationAction.OnPasswordFocusChange -> onPasswordFocusChange(action.isFocused)

            is RegistrationAction.OnPasswordConfirmationFocusChange -> {
                onPasswordConfirmationFocusChange(action.isFocused)
            }

            RegistrationAction.OnAlreadyHaveAccountClick -> onAlreadyHaveAccountClick()

            RegistrationAction.OnCreateAccountClick -> onCreateAccountClick()
        }
    }

    private fun onCreateAccountClick() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }

            val result = with(state.value) {
                userRepository.register(
                    username = username,
                    email = email,
                    password = password.text.toString()
                )
            }

            _state.update {
                it.copy(
                    isLoading = false
                )
            }

            when (result) {
                is Result.Error -> {
                    eventChannel.send(
                        RegistrationEvent.ShowMessage(
                            message = result.error.toUiText()
                        )
                    )
                }

                is Result.Success -> {
                    eventChannel.send(RegistrationEvent.NavigateToLogin)
                }
            }
        }
    }

    private fun onAlreadyHaveAccountClick() {
        viewModelScope.launch {
            eventChannel.send(RegistrationEvent.NavigateToLogin)
        }
    }

    private fun onPasswordConfirmationFocusChange(isFocused: Boolean) {
        _state.update {
            it.copy(
                isPasswordConfirmationFocused = isFocused
            )
        }
    }

    private fun onPasswordFocusChange(isFocused: Boolean) {
        _state.update {
            it.copy(
                isPasswordFocused = isFocused
            )
        }
    }

    private fun onEmailFocusChange(isFocused: Boolean) {
        _state.update {
            it.copy(
                isEmailFocused = isFocused
            )
        }
    }

    private fun onEmailChange(email: String) {
        _state.update {
            it.copy(
                email = email
            )
        }
    }

    private fun onUsernameFocusChange(isFocused: Boolean) {
        _state.update {
            it.copy(
                isUsernameFocused = isFocused
            )
        }
    }

    private fun onUsernameChange(username: String) {
        _state.update {
            it.copy(
                username = username
            )
        }
    }
}