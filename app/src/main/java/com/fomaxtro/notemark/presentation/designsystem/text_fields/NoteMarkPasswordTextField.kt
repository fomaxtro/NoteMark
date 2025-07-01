package com.fomaxtro.notemark.presentation.designsystem.text_fields

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.fomaxtro.notemark.R
import com.fomaxtro.notemark.presentation.designsystem.theme.NoteMarkTheme

@Composable
fun NoteMarkPasswordTextField(
    state: TextFieldState,
    label: String,
    placeholder: String,
    showPassword: Boolean,
    onPasswordVisibilityChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    supportingText: String? = null,
    isError: Boolean = false
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    BasicSecureTextField(
        state = state,
        modifier = modifier
            .defaultMinSize(
                minWidth = NoteMarkTextFieldDefaults.MinWidth,
                minHeight = NoteMarkTextFieldDefaults.MinHeight
            ),
        decorator = { innerTextField ->
            NoteMarkTextFieldDefaults.DecorationBox(
                value = state.text.toString(),
                innerTextField = innerTextField,
                label = label,
                placeholder = placeholder,
                isError = isError,
                supportingText = supportingText,
                trailingIcon = {
                    IconButton(
                        onClick = {
                            onPasswordVisibilityChange(!showPassword)
                        }
                    ) {
                        Icon(
                            imageVector = if (showPassword) {
                                ImageVector.vectorResource(R.drawable.eye_off)
                            } else {
                                ImageVector.vectorResource(R.drawable.eye)
                            },
                            contentDescription = if (showPassword) {
                                stringResource(R.string.hide_password)
                            } else {
                                stringResource(R.string.show_password)
                            }
                        )
                    }
                },
                interactionSource = interactionSource
            )
        },
        interactionSource = interactionSource,
        textStyle = MaterialTheme.typography.bodyLarge,
        cursorBrush = SolidColor(
            if (isError) {
                MaterialTheme.colorScheme.error
            } else MaterialTheme.colorScheme.primary
        ),
        textObfuscationMode = if (showPassword) {
            TextObfuscationMode.Visible
        } else {
            TextObfuscationMode.RevealLastTyped
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun NoteMarkPasswordTextFieldPreview() {
    NoteMarkTheme {
        NoteMarkPasswordTextField(
            state = TextFieldState("sdfsdfsdfs"),
            label = "Password",
            placeholder = "secure password",
            modifier = Modifier
                .fillMaxWidth(),
            showPassword = false,
            onPasswordVisibilityChange = {}
        )
    }
}
