package com.fomaxtro.notemark.presentation.screen.note_details

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fomaxtro.notemark.R
import com.fomaxtro.notemark.domain.model.Note
import com.fomaxtro.notemark.presentation.designsystem.app_bars.NoteMarkTopAppBar
import com.fomaxtro.notemark.presentation.designsystem.buttons.NoteMarkIconToggleButton
import com.fomaxtro.notemark.presentation.designsystem.scaffolds.NoteMarkFabPosition
import com.fomaxtro.notemark.presentation.designsystem.scaffolds.NoteMarkScaffold
import com.fomaxtro.notemark.presentation.designsystem.theme.NoteMarkTheme
import com.fomaxtro.notemark.presentation.mapper.toNoteDetailUi
import com.fomaxtro.notemark.presentation.screen.note_details.components.NoteMetadata
import com.fomaxtro.notemark.presentation.ui.DeviceOrientation
import com.fomaxtro.notemark.presentation.ui.ObserveAsEvents
import com.fomaxtro.notemark.presentation.ui.rememberDeviceOrientation
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

@Composable
fun NoteDetailsRoot(
    id: String,
    navigateBack: () -> Unit,
    navigateToEditNote: (String) -> Unit,
    viewModel: NoteDetailsViewModel = koinViewModel {
        parametersOf(id)
    }
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            NoteDetailsEvent.NavigateBack -> navigateBack()
            is NoteDetailsEvent.NavigateToEditNote -> navigateToEditNote(id)
            NoteDetailsEvent.SetLandscapeOrientation -> {
                (context as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }
            NoteDetailsEvent.SetUnspecifiedOrientation -> {
                (context as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            }
        }
    }

    NoteDetailsScreen(
        onAction = viewModel::onAction,
        state = state
    )
}

@Stable
private fun fadeInSlow(): EnterTransition = fadeIn(
    animationSpec = spring(
        stiffness = Spring.StiffnessLow
    )
)

@Stable
private fun fadeOutSlow(): ExitTransition = fadeOut(
    animationSpec = spring(
        stiffness = Spring.StiffnessLow
    )
)

@Composable
private fun NoteDetailsScreen(
    onAction: (NoteDetailsAction) -> Unit = {},
    state: NoteDetailsState
) {
    val deviceOrientation = rememberDeviceOrientation()
    val scrollState = rememberScrollState()

    LaunchedEffect(scrollState.isScrollInProgress) {
        if (scrollState.isScrollInProgress) {
            onAction(NoteDetailsAction.OnContentScroll)
        }
    }

    NoteMarkScaffold(
        topAppBar = {
            NoteMarkTopAppBar(
                title = {
                    AnimatedVisibility(
                        visible = state.showControls,
                        enter = fadeInSlow(),
                        exit = fadeOutSlow()
                    ) {
                        Text(
                            text = stringResource(R.string.all_notes),
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                },
                navigationIcon = {
                    AnimatedVisibility(
                        visible = state.showControls,
                        enter = fadeInSlow(),
                        exit = fadeOutSlow()
                    ) {
                        IconButton(
                            onClick = {
                                onAction(NoteDetailsAction.OnNavigateBackClick)
                            }
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.chevron_left),
                                contentDescription = stringResource(R.string.navigate_back)
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = state.showControls,
                enter = fadeInSlow(),
                exit = fadeOutSlow()
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row {
                        IconButton(
                            onClick = {
                                onAction(NoteDetailsAction.OnEditNoteClick)
                            }
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.edit),
                                contentDescription = stringResource(R.string.edit)
                            )
                        }

                        NoteMarkIconToggleButton(
                            checked = state.readerMode,
                            onCheckedChange = {
                                onAction(NoteDetailsAction.OnReaderModeClick(it))
                            }
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.book_open),
                                contentDescription = if (state.readerMode) {
                                    stringResource(R.string.view_mode)
                                } else {
                                    stringResource(R.string.reader_mode)
                                }
                            )
                        }
                    }
                }
            }
        },
        floatingActionButtonPosition = NoteMarkFabPosition.CENTER,
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures {
                    onAction(NoteDetailsAction.OnTapScreen)
                }
            }
    ) { innerPadding ->
        if (state.note != null) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .then(
                        if (deviceOrientation == DeviceOrientation.PHONE_TABLET_LANDSCAPE) {
                            Modifier
                                .statusBarsPadding()
                                .width(540.dp)
                                .verticalScroll(scrollState)
                        } else {
                            Modifier
                                .fillMaxWidth()
                                .padding(innerPadding)
                        }
                    )
            ) {
                Text(
                    text = state.note.title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .padding(
                            horizontal = 16.dp,
                            vertical = 20.dp
                        )
                )

                HorizontalDivider()

                NoteMetadata(
                    modifier = Modifier
                        .fillMaxWidth(),
                    createdAt = state.note.createdAt,
                    lastEditedAt = state.note.lastEditedAt.asString()
                )

                HorizontalDivider()

                Text(
                    text = state.note.content,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(
                            horizontal = 16.dp,
                            vertical = 20.dp
                        )
                        .then(
                            if (deviceOrientation == DeviceOrientation.PHONE_TABLET_LANDSCAPE) {
                                Modifier
                            } else {
                                Modifier
                                    .verticalScroll(rememberScrollState())
                            }
                        )
                )
            }
        }
    }
}

internal val previewNote = Note(
    id = UUID.randomUUID(),
    title = "Note Title",
    content = "Lorem ipsum dolor sit amet consectetur adipiscing elit. Quisque faucibus ex sapien vitae pellentesque sem placerat. In id cursus mi pretium tellus duis convallis. Tempus leo eu aenean sed diam urna tempor. Pulvinar vivamus fringilla lacus nec metus bibendum egestas. Iaculis massa nisl malesuada lacinia integer nunc posuere. Ut hendrerit semper vel class aptent taciti sociosqu. Ad litora torquent per conubia nostra inceptos himenaeos.\n" +
            "\n" +
            "Lorem ipsum dolor sit amet consectetur adipiscing elit. Quisque faucibus ex sapien vitae pellentesque sem placerat. In id cursus mi pretium tellus duis convallis. Tempus leo eu aenean sed diam urna tempor. Pulvinar vivamus fringilla lacus nec metus bibendum egestas. Iaculis massa nisl malesuada lacinia integer nunc posuere. Ut hendrerit semper vel class aptent taciti sociosqu. Ad litora torquent per conubia nostra inceptos himenaeos.\n" +
            "\n" +
            "Lorem ipsum dolor sit amet consectetur adipiscing elit. Quisque faucibus ex sapien vitae pellentesque sem placerat. In id cursus mi pretium tellus duis convallis. Tempus leo eu aenean sed diam urna tempor. Pulvinar vivamus fringilla lacus nec metus bibendum egestas. Iaculis massa nisl malesuada lacinia integer nunc posuere. Ut hendrerit semper vel class aptent taciti sociosqu. Ad litora torquent per conubia nostra inceptos himenaeos.\n" +
            "\n" +
            "Lorem ipsum dolor sit amet consectetur adipiscing elit. Quisque faucibus ex sapien vitae pellentesque sem placerat. In id cursus mi pretium tellus duis convallis. Tempus leo eu aenean sed diam urna tempor. Pulvinar vivamus fringilla lacus nec metus bibendum egestas. Iaculis massa nisl malesuada lacinia integer nunc posuere. Ut hendrerit semper vel class aptent taciti sociosqu. Ad litora torquent per conubia nostra inceptos himenaeos.\n" +
            "\n" +
            "Lorem ipsum dolor sit amet consectetur adipiscing elit. Quisque faucibus ex sapien vitae pellentesque sem placerat. In id cursus mi pretium tellus duis convallis. Tempus leo eu aenean sed diam urna tempor. Pulvinar vivamus fringilla lacus nec metus bibendum egestas. Iaculis massa nisl malesuada lacinia integer nunc posuere. Ut hendrerit semper vel class aptent taciti sociosqu. Ad litora torquent per conubia nostra inceptos himenaeos.",
    createdAt = Instant.now(),
    lastEditedAt = Instant.now()
        .minus(3, ChronoUnit.MINUTES)
)

@Preview
@Composable
private fun NoteDetailsScreenPreview() {
    NoteMarkTheme {
        NoteDetailsScreen(
            state = NoteDetailsState(
                note = previewNote.toNoteDetailUi(),
                readerMode = true
            )
        )
    }
}