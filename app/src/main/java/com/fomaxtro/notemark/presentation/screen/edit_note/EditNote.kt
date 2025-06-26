package com.fomaxtro.notemark.presentation.screen.edit_note

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fomaxtro.notemark.R
import com.fomaxtro.notemark.presentation.designsystem.app_bars.NoteMarkTopAppBar
import com.fomaxtro.notemark.presentation.designsystem.theme.NoteMarkTheme
import com.fomaxtro.notemark.presentation.designsystem.theme.SpaceGrotesk
import com.fomaxtro.notemark.presentation.screen.edit_note.components.AdaptiveScaffold
import com.fomaxtro.notemark.presentation.screen.edit_note.components.PlainTextFieldDecorationBox
import com.fomaxtro.notemark.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun EditNoteRoot(
    id: String?,
    navigateBack: () -> Unit,
    viewModel: EditNoteViewModel = koinViewModel {
        parametersOf(id)
    }
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            EditNoteEvent.NavigateBack -> navigateBack()
        }
    }

    EditNoteScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun EditNoteScreen(
    onAction: (EditNoteAction) -> Unit,
    state: EditNoteState
) {


    if (state.showDiscardDialog) {
        AlertDialog(
            onDismissRequest = {
                onAction(EditNoteAction.OnDiscardDialogDismiss)
            },
            title = {
                Text(stringResource(R.string.discard_changes))
            },
            text = {
                Text(stringResource(R.string.discard_changes_message))
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onAction(EditNoteAction.OnDiscardDialogConfirm)
                    }
                ) {
                    Text(stringResource(R.string.discard))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onAction(EditNoteAction.OnDiscardDialogDismiss)
                    }
                ) {
                    Text(stringResource(R.string.keep_editing))
                }
            }
        )
    }

    AdaptiveScaffold(
        topAppBar = {
            NoteMarkTopAppBar(
                title = {
                    IconButton(
                        onClick = {
                            onAction(EditNoteAction.OnDiscardClick)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null
                        )
                    }
                },
                action = {
                    TextButton(
                        onClick = {
                            onAction(EditNoteAction.OnSaveNoteClick)
                        }
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
        },
        modifier = Modifier
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            BasicTextField(
                value = state.title,
                onValueChange = {
                    onAction(EditNoteAction.OnTitleChange(it))
                },
                textStyle = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth(),
                decorationBox = { innerTextField ->
                    PlainTextFieldDecorationBox(
                        value = state.title,
                        innerTextField = innerTextField
                    )
                },
                cursorBrush = SolidColor(
                    MaterialTheme.colorScheme.primary
                )
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.surface
            )

            val contentTextStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            BasicTextField(
                value = state.content,
                onValueChange = {
                    onAction(EditNoteAction.OnContentChange(it))
                },
                textStyle = contentTextStyle,
                modifier = Modifier
                    .fillMaxWidth(),
                decorationBox = { innerTextField ->
                    PlainTextFieldDecorationBox(
                        value = state.content,
                        innerTextField = innerTextField,
                        placeHolder = stringResource(R.string.note_content_placeholder),
                        textStyle = contentTextStyle
                    )
                },
                cursorBrush = SolidColor(
                    MaterialTheme.colorScheme.primary
                ),
            )
        }
    }
}

@Preview
@Composable
private fun EditNoteScreenPreview() {
    NoteMarkTheme {
        EditNoteScreen(
            state = EditNoteState(
                title = "New Note",
                content = "",
                showDiscardDialog = false
            ),
            onAction = {}
        )
    }
}