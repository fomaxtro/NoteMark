package com.fomaxtro.notemark.presentation.screen.edit_note

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fomaxtro.notemark.R
import com.fomaxtro.notemark.presentation.designsystem.app_bars.NoteMarkTopAppBar
import com.fomaxtro.notemark.presentation.designsystem.app_bars.NoteMarkTopAppBarDefaults
import com.fomaxtro.notemark.presentation.designsystem.theme.NoteMarkTheme
import com.fomaxtro.notemark.presentation.designsystem.theme.SpaceGrotesk
import com.fomaxtro.notemark.presentation.ui.DeviceOrientation
import com.fomaxtro.notemark.presentation.ui.rememberDeviceOrientation
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun EditNoteRoot(
    id: String?,
    viewModel: EditNoteViewModel = koinViewModel {
        parametersOf(id)
    }
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    EditNoteScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun AdaptiveScaffold(
    topAppBar: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    val deviceOrientation = rememberDeviceOrientation()

    if (deviceOrientation == DeviceOrientation.PHONE_TABLET_LANDSCAPE) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            topAppBar()

            Box(
                modifier = Modifier
                    .width(540.dp)
                    .fillMaxHeight()
                    .align(Alignment.TopCenter)
                    .windowInsetsPadding(NoteMarkTopAppBarDefaults.windowInsets)
                    .consumeWindowInsets(NoteMarkTopAppBarDefaults.windowInsets)
                    .verticalScroll(rememberScrollState())
            ) {
                content()
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            topAppBar()
            content()
        }
    }
}

@Composable
private fun EditNoteScreen(
    onAction: (EditNoteAction) -> Unit,
    state: EditNoteState
) {
    val contentPadding = PaddingValues(
        horizontal = 16.dp,
        vertical = 20.dp
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        color = MaterialTheme.colorScheme.surfaceContainerLowest
    ) {
        AdaptiveScaffold(
            topAppBar = {
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
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                BasicTextField(
                    value = state.title,
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
                    value = state.content,
                    onValueChange = {},
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier
                        .fillMaxWidth(),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .padding(contentPadding)
                        ) {
                            innerTextField()
                        }
                    },
                    cursorBrush = SolidColor(
                        MaterialTheme.colorScheme.primary
                    ),
                )
            }
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
                content = "Amet minim mollit non deserunt ullamco est sit aliqua dolor do amet sint. "
            ),
            onAction = {}
        )
    }
}