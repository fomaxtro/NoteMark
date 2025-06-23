package com.fomaxtro.notemark.presentation.edit_note

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fomaxtro.notemark.R
import com.fomaxtro.notemark.presentation.designsystem.app_bars.NoteMarkTopAppBar
import com.fomaxtro.notemark.presentation.designsystem.theme.NoteMarkTheme
import com.fomaxtro.notemark.presentation.designsystem.theme.SpaceGrotesk

@Composable
fun EditNoteRoot() {
    EditNoteScreen()
}

@Composable
private fun EditNoteScreen() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        color = MaterialTheme.colorScheme.surfaceContainerLowest
    ) {
        val contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 20.dp
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            NoteMarkTopAppBar(
                title = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null
                        )
                    }
                },
                action = {
                    TextButton(
                        onClick = {}
                    ) {
                        Text(
                            text = stringResource(R.string.save_note),
                            fontFamily = SpaceGrotesk,
                            fontSize = 16.sp,
                            lineHeight = 24.sp
                        )
                    }
                }
            )

            BasicTextField(
                value = "Note Title",
                onValueChange = {},
                textStyle = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth(),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(contentPadding)
                    ) {
                        innerTextField()
                    }
                },
                cursorBrush = SolidColor(
                    MaterialTheme.colorScheme.primary
                )
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.surface
            )

            BasicTextField(
                value = "Conent text",
                onValueChange = {},
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(contentPadding)
                    ) {
                        innerTextField()
                    }
                },
                cursorBrush = SolidColor(
                    MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

@Preview
@Composable
private fun EditNoteScreenPreview() {
    NoteMarkTheme {
        EditNoteScreen()
    }
}