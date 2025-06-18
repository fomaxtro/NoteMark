package com.fomaxtro.notemark.presentation.designsystem.text_fields

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    val isFocused by interactionSource.collectIsFocusedAsState()

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .widthIn(
                min = OutlinedTextFieldDefaults.MinWidth
            )
            .heightIn(
                min = OutlinedTextFieldDefaults.MinHeight
            )
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier.fillMaxWidth(),
            decorationBox = { innerTextField ->
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = if (isFocused || isError) {
                        Color.Transparent
                    } else MaterialTheme.colorScheme.surface,
                    border = BorderStroke(
                        color = when {
                            isError -> MaterialTheme.colorScheme.error
                            isFocused -> MaterialTheme.colorScheme.primary
                            else -> Color.Transparent
                        },
                        width = 1.dp
                    )
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(OutlinedTextFieldDefaults.contentPadding())
                        ) {
                            if (value.isEmpty() && placeholder.isNotEmpty()) {
                                Text(
                                    text = placeholder,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(start = 2.dp)
                                )
                            }

                            innerTextField()
                        }

                        Box(
                            modifier = Modifier
                                .height(IntrinsicSize.Min)
                                .width(IntrinsicSize.Min)
                        ) {
                            trailingIcon?.invoke()
                        }
                    }
                }
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

        AnimatedVisibility(
            visible = supportingText != null
        ) {
            Text(
                text = supportingText ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = if (isError) {
                    MaterialTheme.colorScheme.error
                } else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
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
