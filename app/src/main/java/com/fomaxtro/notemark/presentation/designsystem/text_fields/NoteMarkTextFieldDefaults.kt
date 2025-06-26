package com.fomaxtro.notemark.presentation.designsystem.text_fields

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object NoteMarkTextFieldDefaults {
    val MinWidth = TextFieldDefaults.MinWidth
    val MinHeight = TextFieldDefaults.MinHeight

    @Composable
    fun DecorationBox(
        value: String,
        innerTextField: @Composable () -> Unit,
        label: String,
        placeholder: String,
        interactionSource: InteractionSource,
        trailingIcon: @Composable (() -> Unit)?,
        isError: Boolean,
        supportingText: String?
    ) {
        val isFocused by interactionSource.collectIsFocusedAsState()

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium
            )

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
}