package com.fomaxtro.notemark.presentation.screen.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fomaxtro.notemark.R
import com.fomaxtro.notemark.presentation.designsystem.buttons.NoteMarkButton
import com.fomaxtro.notemark.presentation.designsystem.text_fields.NoteMarkPasswordTextField
import com.fomaxtro.notemark.presentation.designsystem.text_fields.NoteMarkTextField
import com.fomaxtro.notemark.presentation.designsystem.theme.NoteMarkTheme
import com.fomaxtro.notemark.presentation.screen.components.AdaptiveTwoPaneSheet
import com.fomaxtro.notemark.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginRoot(
    navigateToHome: () -> Unit,
    navigateToRegistration: () -> Unit,
    viewModel: LoginViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            LoginEvent.NavigateToHome -> navigateToHome()
            LoginEvent.NavigateToRegistration -> navigateToRegistration()
        }
    }
    
    LoginScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun LoginScreen(
    onAction: (LoginAction) -> Unit,
    state: LoginState
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.primary,
        contentWindowInsets = WindowInsets.statusBars
    ) { innerPadding ->
        AdaptiveTwoPaneSheet(
            title = stringResource(R.string.log_in),
            subtitle = stringResource(R.string.log_in_subtitle),
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
                    value = state.email,
                    onValueChange = {
                        onAction(LoginAction.OnEmailChange(it))
                    },
                    label = stringResource(R.string.email),
                    placeholder = stringResource(R.string.email_placeholder)
                )

                NoteMarkPasswordTextField(
                    value = state.password,
                    onValueChange = {
                        onAction(LoginAction.OnPasswordChange(it))
                    },
                    label = stringResource(R.string.password),
                    placeholder = stringResource(R.string.password),
                    showPassword = state.isPasswordVisible,
                    onPasswordVisibilityChange = {
                        onAction(LoginAction.OnPasswordVisibilityChange(it))
                    }
                )

                NoteMarkButton(
                    onClick = {
                        onAction(LoginAction.OnLogInClick)
                    },
                    text = stringResource(R.string.log_in),
                    modifier = Modifier
                        .fillMaxWidth(),
                    enabled = state.canLogin,
                    isLoading = state.isLoading
                )

                TextButton(
                    onClick = {
                        onAction(LoginAction.OnDontHaveAccountClick)
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = stringResource(R.string.dont_have_an_account),
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    NoteMarkTheme {
        LoginScreen(
            state = LoginState(),
            onAction = {}
        )
    }
}
