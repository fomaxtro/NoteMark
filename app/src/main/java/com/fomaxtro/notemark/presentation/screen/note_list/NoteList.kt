package com.fomaxtro.notemark.presentation.screen.note_list

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fomaxtro.notemark.R
import com.fomaxtro.notemark.presentation.designsystem.app_bars.NoteMarkTopAppBar
import com.fomaxtro.notemark.presentation.designsystem.buttons.NoteMarkFloatingActionButton
import com.fomaxtro.notemark.presentation.designsystem.theme.NoteMarkTheme
import com.fomaxtro.notemark.presentation.model.NoteUi
import com.fomaxtro.notemark.presentation.screen.note_list.components.Avatar
import com.fomaxtro.notemark.presentation.screen.note_list.components.NoteCardItem
import com.fomaxtro.notemark.presentation.ui.DeviceOrientation
import com.fomaxtro.notemark.presentation.ui.ObserveAsEvents
import com.fomaxtro.notemark.presentation.ui.rememberDeviceOrientation
import org.koin.androidx.compose.koinViewModel
import java.util.UUID

@Composable
fun NoteListRoot(
    navigateToEditNote: (id: UUID) -> Unit,
    viewModel: NoteListViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is NoteListEvent.NavigateToEditNote -> navigateToEditNote(event.id)
            is NoteListEvent.ShowSystemMessage -> {
                Toast.makeText(
                    context,
                    event.message.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    NoteListScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun NoteListScreen(
    onAction: (NoteListAction) -> Unit,
    state: NoteListState
) {
    val deviceOrientation = rememberDeviceOrientation()

    val contentPadding = if (deviceOrientation == DeviceOrientation.PHONE_TABLET_LANDSCAPE) {
        PaddingValues(start = 60.dp)
    } else PaddingValues()

    Scaffold(
        topBar = {
            NoteMarkTopAppBar(
                title = {
                    Text(text = stringResource(R.string.app_name))
                },
                action = {
                    Avatar(name = state.username)
                },
                contentPadding = contentPadding
            )
        },
        floatingActionButton = {
            NoteMarkFloatingActionButton(
                onClick = {
                    onAction(NoteListAction.OnNewNoteClick)
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        if (state.notes.isNotEmpty()) {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(contentPadding),
                contentPadding = PaddingValues(16.dp),
                verticalItemSpacing = 16.dp,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(state.notes) { note ->
                    NoteCardItem(
                        date = note.createdAt,
                        title = note.title,
                        content = note.content,
                        onClick = {
                            onAction(NoteListAction.OnNoteClick(note.id))
                        }
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .padding(top = 80.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    text = stringResource(R.string.empty_note_list_placeholder),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

internal val previewNoteUi = NoteUi(
    id = UUID.randomUUID(),
    title = "Title",
    content = "Content",
    createdAt = "23 Jan 2024"
)

@Preview
@Composable
private fun NoteListScreenPreview() {
    val notes = listOf(
        previewNoteUi.copy(
            content = "Augue non mauris ante viverra ut arcu sed ut "
        ),
        previewNoteUi.copy(
            content = "Augue non mauris ante viverra ut arcu sed ut lectus interdum morbi sed leo purus gravida non id mi augue."
        ),
        previewNoteUi.copy(
            content = "Augue non mauris ante viverra ut arcu sed ut lectus interdum morbi sed leo purus gravida non id mi augue."
        )
    )

    NoteMarkTheme {
        NoteListScreen(
            state = NoteListState(
                notes = notes,
                username = "NA"
            ),
            onAction = {}
        )
    }
}