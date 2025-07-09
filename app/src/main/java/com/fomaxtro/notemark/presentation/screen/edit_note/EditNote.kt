package com.fomaxtro.notemark.presentation.screen.edit_note

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fomaxtro.notemark.R
import com.fomaxtro.notemark.presentation.designsystem.app_bars.NoteMarkTopAppBar
import com.fomaxtro.notemark.presentation.designsystem.scaffolds.NoteMarkScaffold
import com.fomaxtro.notemark.presentation.designsystem.text_fields.AutoScrolledBasicTextField
import com.fomaxtro.notemark.presentation.designsystem.theme.NoteMarkTheme
import com.fomaxtro.notemark.presentation.designsystem.theme.SpaceGrotesk
import com.fomaxtro.notemark.presentation.screen.edit_note.components.PlainTextFieldDecorationBox
import com.fomaxtro.notemark.presentation.ui.DeviceOrientation
import com.fomaxtro.notemark.presentation.ui.ObserveAsEvents
import com.fomaxtro.notemark.presentation.ui.rememberDeviceOrientation
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun EditNoteRoot(
    id: String,
    navigateBack: () -> Unit,
    viewModel: EditNoteViewModel = koinViewModel {
        parametersOf(id)
    }
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val focusRequester = remember { FocusRequester() }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            EditNoteEvent.NavigateBack -> navigateBack()
            EditNoteEvent.RequestTitleFocus -> focusRequester.requestFocus()
        }
    }

    EditNoteScreen(
        state = state,
        onAction = viewModel::onAction,
        focusRequester = focusRequester
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EditNoteScreen(
    onAction: (EditNoteAction) -> Unit = {},
    state: EditNoteState,
    focusRequester: FocusRequester
) {
    val focusManager = LocalFocusManager.current
    val deviceOrientation = rememberDeviceOrientation()

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

    NoteMarkScaffold(
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
                            contentDescription = stringResource(R.string.cancel)
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
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus()
                }
            }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .then(
                    if (deviceOrientation == DeviceOrientation.PHONE_TABLET_LANDSCAPE) {
                        Modifier
                            .width(540.dp)
                            .verticalScroll(rememberScrollState())
                            .imePadding()
                    } else {
                        Modifier
                            .fillMaxWidth()
                            .padding(innerPadding)
                    }
                )
        ) {
            BasicTextField(
                value = state.title,
                onValueChange = {
                    onAction(EditNoteAction.OnTitleChange(it))
                },
                textStyle = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                decorationBox = { innerTextField ->
                    PlainTextFieldDecorationBox(
                        value = state.title.text,
                        innerTextField = innerTextField,
                        placeholder = stringResource(R.string.note_title),
                        textStyle = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                },
                cursorBrush = SolidColor(
                    MaterialTheme.colorScheme.primary
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                )
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.surface
            )

            val contentTextStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            AutoScrolledBasicTextField(
                state = state.content,
                textStyle = contentTextStyle,
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (deviceOrientation == DeviceOrientation.PHONE_TABLET_LANDSCAPE) {
                            Modifier
                        } else {
                            Modifier.weight(1f)
                        }
                    )
                    .imePadding(),
                decorator = { innerTextField ->
                    PlainTextFieldDecorationBox(
                        value = state.content.text.toString(),
                        innerTextField = innerTextField,
                        placeholder = stringResource(R.string.note_content_placeholder),
                        textStyle = contentTextStyle
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                )
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
                title = TextFieldValue("New Note"),
                content = TextFieldState(
                    "The quiet hum of the morning air was broken only by the soft rustle of leaves dancing in the breeze. A cup of coffee steamed gently in hand, warmth seeping into chilled fingers. Thoughts flowed freely, unbothered by structure or form—just fragments of memory and hope stitched together. In this stillness, inspiration felt close, like a familiar friend waiting to be acknowledged and welcomed in. Pages remained blank, yet full of potential. Each pause between thoughts was its own kind of music. The world outside hadn’t changed, but the lens through which it was viewed felt freshly cleaned—clearer, softer. Clouds moved lazily across the sky, painting slow-moving stories above. A breeze swept in, carrying with it the faint scent of earth and something that reminded one of home. The clinking of a spoon against a ceramic mug, the distant bark of a dog, a laugh from a neighbor’s open window—each moment ordinary, yet profound. And somehow, all of it seemed to whisper: write, because this matters."
                ),
                showDiscardDialog = false
            ),
            focusRequester = FocusRequester()
        )
    }
}