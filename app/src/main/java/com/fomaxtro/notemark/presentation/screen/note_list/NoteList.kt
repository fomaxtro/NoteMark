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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fomaxtro.notemark.R
import com.fomaxtro.notemark.presentation.designsystem.app_bars.NoteMarkTopAppBar
import com.fomaxtro.notemark.presentation.designsystem.buttons.NoteMarkFloatingActionButton
import com.fomaxtro.notemark.presentation.designsystem.theme.NoteMarkTheme
import com.fomaxtro.notemark.presentation.screen.note_list.components.Avatar
import com.fomaxtro.notemark.presentation.screen.note_list.components.NoteCardItem
import com.fomaxtro.notemark.presentation.screen.note_list.model.NoteUi
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
fun NoteListRoot() {
    NoteListScreen(
        state = NoteListState()
    )
}

@Composable
private fun NoteListScreen(
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
                onClick = {}
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

@OptIn(ExperimentalUuidApi::class)
internal val previewNoteUi = NoteUi(
    id = Uuid.random(),
    title = "Title",
    content = "Content",
    createdAt = "23 Jan 2024"
)

@OptIn(ExperimentalUuidApi::class)
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
            )
        )
    }
}