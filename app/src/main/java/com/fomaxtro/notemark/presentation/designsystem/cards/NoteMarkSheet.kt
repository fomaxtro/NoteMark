package com.fomaxtro.notemark.presentation.designsystem.cards

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fomaxtro.notemark.presentation.designsystem.theme.NoteMarkTheme

@Composable
fun NoteMarkSheet(
    modifier: Modifier = Modifier,
    shape: Shape = NoteMarkSheetDefaults.shape,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = shape,
        colors = NoteMarkSheetDefaults.colors,
        content = content
    )
}

@Preview
@Composable
private fun NoteMarkSheetPreview() {
    NoteMarkTheme {
        NoteMarkSheet(
            modifier = Modifier
                .size(
                    width = 200.dp,
                    height = 100.dp
                ),
            content = {}
        )
    }
}
