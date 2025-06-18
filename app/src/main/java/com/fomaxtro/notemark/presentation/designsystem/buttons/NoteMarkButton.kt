package com.fomaxtro.notemark.presentation.designsystem.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fomaxtro.notemark.R
import com.fomaxtro.notemark.presentation.designsystem.theme.NoteMarkTheme

@Composable
fun NoteMarkButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isLoading: Boolean = false
) {
    val padding = with(ButtonDefaults.ContentPadding) {
        calculateTopPadding() + calculateBottomPadding()
    }
    val heightWithPadding = ButtonDefaults.MinHeight - padding

    Button(
        onClick = {
            if (!isLoading) {
                onClick()
            }
        },
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        contentPadding = NoteMarkButtonDefaults.contentPadding,
        enabled = enabled
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(heightWithPadding),
                color = MaterialTheme.colorScheme.onPrimary
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                leadingIcon?.invoke()

                Text(
                    text = text,
                    style = NoteMarkButtonDefaults.textStyle
                )

                trailingIcon?.invoke()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NoteMarkButtonPreview() {
    var loading by remember { mutableStateOf(false) }

    NoteMarkTheme {
        NoteMarkButton(
            onClick = {
                loading = true
            },
            text = "Label",
            leadingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.copy),
                    contentDescription = null
                )
            },
            enabled = true,
            isLoading = loading
        )
    }
}
