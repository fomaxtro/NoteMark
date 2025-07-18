package com.fomaxtro.notemark.presentation.screen.registration

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fomaxtro.notemark.R
import com.fomaxtro.notemark.presentation.designsystem.buttons.NoteMarkButton
import com.fomaxtro.notemark.presentation.designsystem.sheets.AdaptiveTwoPaneSheet
import com.fomaxtro.notemark.presentation.designsystem.text_fields.NoteMarkPasswordTextField
import com.fomaxtro.notemark.presentation.designsystem.text_fields.NoteMarkTextField
import com.fomaxtro.notemark.presentation.designsystem.theme.NoteMarkTheme
import com.fomaxtro.notemark.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegistrationRoot(
    navigateToLogin: () -> Unit,
    viewModel: RegistrationViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            RegistrationEvent.NavigateToLogin -> navigateToLogin()
            is RegistrationEvent.ShowMessage -> {
                snackbarHostState.showSnackbar(
                    message = event.message.asString(context)
                )
            }
        }
    }

    RegistrationScreen(
        onAction = viewModel::onAction,
        state = state,
        snackbarHostState = snackbarHostState
    )
}

@Composable
private fun RegistrationScreen(
    onAction: (RegistrationAction) -> Unit = {},
    snackbarHostState: SnackbarHostState,
    state: RegistrationState
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        containerColor = MaterialTheme.colorScheme.primary,
        contentWindowInsets = WindowInsets.statusBars,
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus()
                }
            },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.systemBarsPadding()
            )
        }
    ) { innerPadding ->
        AdaptiveTwoPaneSheet(
            title = stringResource(R.string.create_account),
            subtitle = stringResource(R.string.registration_subtitle),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                NoteMarkTextField(
                    value = state.username,
                    onValueChange = {
                        onAction(RegistrationAction.OnUsernameChange(it))
                    },
                    label = stringResource(R.string.username),
                    placeholder = stringResource(R.string.username_placeholder),
                    supportingText = if (state.isUsernameFocused) {
                        stringResource(R.string.username_hint)
                    } else if (state.isUsernameError) {
                        state.usernameError?.asString()
                    } else {
                        null
                    },
                    isError = state.isUsernameError,
                    modifier = Modifier
                        .onFocusChanged {
                            onAction(RegistrationAction.OnUsernameFocusChange(it.isFocused))
                        },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    )
                )

                NoteMarkTextField(
                    value = state.email,
                    onValueChange = {
                        onAction(RegistrationAction.OnEmailChange(it))
                    },
                    label = stringResource(R.string.email),
                    placeholder = stringResource(R.string.email_placeholder),
                    isError = state.isEmailError,
                    supportingText = if (state.isEmailError) {
                        state.emailError?.asString()
                    } else null,
                    modifier = Modifier
                        .onFocusChanged {
                            onAction(RegistrationAction.OnEmailFocusChange(it.isFocused))
                        },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    )
                )

                NoteMarkPasswordTextField(
                    state = state.password,
                    label = stringResource(R.string.password),
                    placeholder = stringResource(R.string.password),
                    supportingText = if (state.isPasswordFocused) {
                        stringResource(R.string.password_hint)
                    } else if (state.isPasswordError) {
                        state.passwordError?.asString()
                    } else {
                        null
                    },
                    isError = state.isPasswordError,
                    modifier = Modifier
                        .onFocusChanged {
                            onAction(RegistrationAction.OnPasswordFocusChange(it.isFocused))
                        }
                )

                NoteMarkPasswordTextField(
                    state = state.passwordConfirmation,
                    label = stringResource(R.string.repeat_password),
                    placeholder = stringResource(R.string.password),
                    isError = state.isPasswordConfirmationError,
                    supportingText = if (state.isPasswordConfirmationError) {
                        state.passwordConfirmationError?.asString()
                    } else null,
                    modifier = Modifier
                        .onFocusChanged {
                            onAction(RegistrationAction.OnPasswordConfirmationFocusChange(it.isFocused))
                        }
                )

                NoteMarkButton(
                    onClick = {
                        onAction(RegistrationAction.OnCreateAccountClick)
                    },
                    text = stringResource(R.string.create_account),
                    modifier = Modifier
                        .fillMaxWidth(),
                    enabled = state.canCreateAccount,
                    isLoading = state.isLoading
                )

                TextButton(
                    onClick = {
                        onAction(RegistrationAction.OnAlreadyHaveAccountClick)
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = stringResource(R.string.existing_account),
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun RegistrationScreenPreview() {
    NoteMarkTheme {
        RegistrationScreen(
            state = RegistrationState(),
            snackbarHostState = SnackbarHostState()
        )
    }
}