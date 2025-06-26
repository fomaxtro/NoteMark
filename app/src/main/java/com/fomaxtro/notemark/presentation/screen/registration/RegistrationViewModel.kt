package com.fomaxtro.notemark.presentation.screen.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fomaxtro.notemark.R
import com.fomaxtro.notemark.domain.repository.UserRepository
import com.fomaxtro.notemark.domain.util.Result
import com.fomaxtro.notemark.domain.util.ValidationResult
import com.fomaxtro.notemark.domain.validator.RegistrationDataValidator
import com.fomaxtro.notemark.presentation.mapper.toUiText
import com.fomaxtro.notemark.presentation.ui.UiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegistrationViewModel(
    private val registrationDataValidator: RegistrationDataValidator,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _state = MutableStateFlow(RegistrationState())
    val state = _state
        .onStart {
            startUsernameEvents()
            startEmailEvents()
            startPasswordEvents()
            startPasswordConfirmationEvents()
            startRegisterEvents()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            RegistrationState()
        )

    private val eventChannel = Channel<RegistrationEvent>()
    val events = eventChannel.receiveAsFlow()

    private val validateUsername = _state
        .distinctUntilChangedBy { it.username }
        .map { state ->
            registrationDataValidator.validateUsername(state.username)
        }
        .onEach { validationResult ->
            when (validationResult) {
                is ValidationResult.Error -> {
                    _state.update {
                        it.copy(
                            usernameError = validationResult.error.toUiText()
                        )
                    }
                }

                ValidationResult.Success -> {
                    _state.update {
                        it.copy(
                            usernameError = null
                        )
                    }
                }
            }
        }
        .map { validationResult ->
            validationResult is ValidationResult.Success
        }

    private val validateEmail = _state
        .distinctUntilChangedBy { it.email }
        .map { state ->
            registrationDataValidator.validateEmail(state.email)
        }
        .onEach { validationResult ->
            when (validationResult) {
                is ValidationResult.Error -> {
                    _state.update {
                        it.copy(
                            emailError = validationResult.error.toUiText(),
                        )
                    }
                }

                ValidationResult.Success -> {
                    _state.update {
                        it.copy(
                            emailError = null,
                        )
                    }
                }
            }
        }
        .map { validationResult ->
            validationResult is ValidationResult.Success
        }

    private val validatePassword = _state
        .distinctUntilChangedBy { it.password }
        .map { state ->
            registrationDataValidator.validatePassword(state.password)
        }
        .onEach { validationResult ->
            when (validationResult) {
                is ValidationResult.Error -> {
                    _state.update {
                        it.copy(
                            passwordError = validationResult.error.toUiText(),
                        )
                    }
                }

                ValidationResult.Success -> {
                    _state.update {
                        it.copy(
                            passwordError = null,
                        )
                    }
                }
            }
        }
        .map { validationResult ->
            validationResult is ValidationResult.Success
        }

    private val validatePasswordConfirmation = _state
        .distinctUntilChanged { old, new ->
            old.passwordConfirmation == new.passwordConfirmation
                    && old.password == new.password
        }
        .map { state ->
            registrationDataValidator.validatePasswordConfirmation(
                password = state.password,
                passwordConfirmation = state.passwordConfirmation
            )
        }
        .onEach { validationResult ->
            when (validationResult) {
                is ValidationResult.Error -> {
                    _state.update {
                        it.copy(
                            passwordConfirmationError = validationResult.error.toUiText(),
                        )
                    }
                }

                ValidationResult.Success -> {
                    _state.update {
                        it.copy(
                            passwordConfirmationError = null,
                        )
                    }
                }
            }
        }
        .map { validationResult ->
            validationResult is ValidationResult.Success
        }

    private fun startUsernameEvents() {
        _state
            .distinctUntilChangedBy { it.isFocusedUsername }
            .combine(validateUsername) { state, isValidUsername ->
                _state.update { it.copy(
                    usernameHint = if (state.isFocusedUsername) {
                        UiText.StringResource(R.string.username_hint)
                    } else{
                        null
                    },
                    isUsernameError = !state.isFocusedUsername
                            && state.username.isNotEmpty()
                            && !isValidUsername
                ) }
            }
            .launchIn(viewModelScope)
    }

    private fun startEmailEvents() {
        _state
            .distinctUntilChangedBy { it.isFocusedEmail }
            .combine(validateEmail) { state, isValidEmail ->
                _state.update {
                    it.copy(
                        isEmailError = !state.isFocusedEmail
                                && state.email.isNotEmpty()
                                && !isValidEmail
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun startPasswordEvents() {
        _state
            .distinctUntilChangedBy { it.isFocusedPassword }
            .combine(validatePassword) { state, isValidPassword ->
                _state.update {
                    it.copy(
                        passwordHint = if (state.isFocusedPassword) {
                            UiText.StringResource(R.string.password_hint)
                        } else null,
                        isPasswordError = !state.isFocusedPassword
                                && state.password.isNotEmpty()
                                && !isValidPassword,
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun startPasswordConfirmationEvents() {
        _state
            .distinctUntilChangedBy { it.isFocusedPasswordConfirmation }
            .combine(validatePasswordConfirmation) { state, isValidPasswordConfirmation ->
                _state.update {
                    it.copy(
                        isPasswordConfirmationError = !state.isFocusedPasswordConfirmation
                                && state.passwordConfirmation.isNotEmpty()
                                && !isValidPasswordConfirmation,
                    )
                }

            }
            .launchIn(viewModelScope)
    }

    private fun startRegisterEvents() {
        combine(
            validateUsername,
            validateEmail,
            validatePassword,
            validatePasswordConfirmation
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
            is RegistrationAction.OnPasswordChange -> onPasswordChange(action.password)
            is RegistrationAction.OnPasswordFocusChange -> onPasswordFocusChange(action.isFocused)
            is RegistrationAction.OnPasswordVisibilityChange -> {
                onPasswordVisibilityChange(action.isVisible)
            }

            is RegistrationAction.OnPasswordConfirmationChange -> {
                onPasswordConfirmationChange(action.passwordConfirmation)
            }

            is RegistrationAction.OnPasswordConfirmationFocusChange -> {
                onPasswordConfirmationFocusChange(action.isFocused)
            }

            is RegistrationAction.OnPasswordConfirmationVisibilityChange -> {
                onPasswordConfirmationVisibilityChange(action.isVisible)
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
                    password = password
                )
            }

            _state.update {
                it.copy(
                    isLoading = false
                )
            }

            when (result) {
                is Result.Error -> {
                    eventChannel.send(RegistrationEvent.ShowMessage(
                        message = result.error.toUiText()
                    ))
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

    private fun onPasswordConfirmationVisibilityChange(visible: Boolean) {
        _state.update {
            it.copy(
                showPasswordConfirmation = visible
            )
        }
    }

    private fun onPasswordConfirmationFocusChange(focused: Boolean) {
        _state.update {
            it.copy(
                isFocusedPasswordConfirmation = focused
            )
        }
    }

    private fun onPasswordConfirmationChange(passwordConfirmation: String) {
        _state.update {
            it.copy(
                passwordConfirmation = passwordConfirmation
            )
        }
    }

    private fun onPasswordVisibilityChange(visible: Boolean) {
        _state.update {
            it.copy(
                showPassword = visible
            )
        }
    }

    private fun onPasswordFocusChange(focused: Boolean) {
        _state.update {
            it.copy(
                isFocusedPassword = focused
            )
        }
    }

    private fun onPasswordChange(password: String) {
        _state.update {
            it.copy(
                password = password
            )
        }
    }

    private fun onEmailFocusChange(focused: Boolean) {
        _state.update {
            it.copy(
                isFocusedEmail = focused
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

    private fun onUsernameFocusChange(focused: Boolean) {
        _state.update {
            it.copy(
                isFocusedUsername = focused
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