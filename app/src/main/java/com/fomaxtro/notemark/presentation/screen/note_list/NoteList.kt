package com.fomaxtro.notemark.presentation.screen.note_list

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.fomaxtro.notemark.R
import com.fomaxtro.notemark.presentation.designsystem.app_bars.NoteMarkTopAppBar
import com.fomaxtro.notemark.presentation.designsystem.theme.NoteMarkTheme

@Composable
private fun NoteListScreen() {
    Scaffold(
        topBar = {
            NoteMarkTopAppBar(
                title = stringResource(R.string.app_name),
                name = "Fomaxtro"
            )
        }
    ) { innerPadding ->
        
    }
}

@Preview
@Composable
private fun NoteListScreenPreview() {
    NoteMarkTheme {
        NoteListScreen()
    }
}