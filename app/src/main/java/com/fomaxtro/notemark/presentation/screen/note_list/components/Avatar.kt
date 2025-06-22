package com.fomaxtro.notemark.presentation.screen.note_list.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fomaxtro.notemark.presentation.designsystem.theme.NoteMarkTheme

@Composable
fun Avatar(
    modifier: Modifier = Modifier,
    name: String
) {
    val initials = remember(name) {
        val tokens = name
            .trim()
            .uppercase()
            .split(" ")

        if (tokens.size > 1) {
            val first = tokens
                .first()
                .take(1)
            val last = tokens
                .last()
                .take(1)

            "$first$last"
        } else {
            tokens.first().take(2)
        }
    }

    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.primary,
        shape = MaterialTheme.shapes.medium
    ) {
        Text(
            text = initials,
            modifier = Modifier
                .padding(10.dp)
        )
    }
}

@Preview
@Composable
private fun AvatarPreview() {
    NoteMarkTheme { 
        Avatar(
            name = "Foo"
        )
    }
}