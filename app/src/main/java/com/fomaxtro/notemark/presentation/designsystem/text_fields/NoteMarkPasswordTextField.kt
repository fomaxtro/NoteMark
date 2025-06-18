package com.fomaxtro.notemark.presentation.designsystem.text_fields

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.fomaxtro.notemark.R
import com.fomaxtro.notemark.presentation.designsystem.theme.NoteMarkTheme

@Composable
fun NoteMarkPasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    showPassword: Boolean,
    onPasswordVisibilityChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    supportingText: String? = null,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Password
    ),
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    NoteMarkTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        placeholder = placeholder,
        modifier = modifier,
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
                    contentDescription = null
                )
            }
        },
        visualTransformation = if (showPassword) {
            VisualTransformation.None
        } else PasswordVisualTransformation('*'),
        supportingText = supportingText,
        isError = isError,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )
}

@Preview(showBackground = true)
@Composable
private fun NoteMarkPasswordTextFieldPreview() {
    NoteMarkTheme {
        NoteMarkPasswordTextField(
            value = "sdfsdfsdfs",
            onValueChange = {},
            label = "Password",
            placeholder = "secure password",
            modifier = Modifier
                .fillMaxWidth(),
            showPassword = false,
            onPasswordVisibilityChange = {}
        )
    }
}
