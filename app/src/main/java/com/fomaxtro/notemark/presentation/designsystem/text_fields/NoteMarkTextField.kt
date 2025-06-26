package com.fomaxtro.notemark.presentation.designsystem.text_fields

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.fomaxtro.notemark.presentation.designsystem.theme.NoteMarkTheme

@Composable
fun NoteMarkTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    supportingText: String? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .defaultMinSize(
                minWidth = NoteMarkTextFieldDefaults.MinWidth,
                minHeight = NoteMarkTextFieldDefaults.MinHeight
            ),
        decorationBox = { innerTextField ->
            NoteMarkTextFieldDefaults.DecorationBox(
                value = value,
                innerTextField = innerTextField,
                label = label,
                placeholder = placeholder,
                isError = isError,
                supportingText = supportingText,
                trailingIcon = trailingIcon,
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
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )
}

@Preview(showBackground = true)
@Composable
private fun NoteMarkTextFieldPreview() {
    NoteMarkTheme {
        NoteMarkTextField(
            value = "",
            onValueChange = {},
            label = "Label",
            placeholder = "Placeholder",
            supportingText = "Supporting text ",
            isError = false,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}
