package com.fomaxtro.notemark.presentation.screen.note_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fomaxtro.notemark.R
import com.fomaxtro.notemark.presentation.designsystem.app_bars.NoteMarkTopAppBar
import com.fomaxtro.notemark.presentation.designsystem.buttons.NoteMarkFloatingActionButton
import com.fomaxtro.notemark.presentation.designsystem.theme.NoteMarkTheme
import com.fomaxtro.notemark.presentation.screen.note_list.components.Avatar
import com.fomaxtro.notemark.presentation.screen.note_list.components.NoteCardItem
import com.fomaxtro.notemark.presentation.screen.note_list.model.NoteUi
import com.fomaxtro.notemark.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel
import java.util.UUID

@Composable
fun NoteListRoot(
    navigateToEditNote: () -> Unit,
    viewModel: NoteListViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            NoteListEvent.NavigateToEditNote -> navigateToEditNote()
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
    WindowInsets.statusBars
    Scaffold(
        topBar = {
            NoteMarkTopAppBar(
                title = {
                    Text(text = stringResource(R.string.app_name))
                },
                action = {
                    Avatar(name = "Foo")
                }
            )
        },
        floatingActionButton = {
            NoteMarkFloatingActionButton(
                onClick = {
                    onAction(NoteListAction.OnNewNoteClick)
                }
            )
        },
    ) { innerPadding ->
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalItemSpacing = 16.dp,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(state.notes) { note ->
                NoteCardItem(
                    date = note.createdAt,
                    title = note.title,
                    content = note.content
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
    NoteMarkTheme {
        NoteListScreen(
            state = NoteListState(
                notes = listOf(
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
            ),
            onAction = {}
        )
    }
}