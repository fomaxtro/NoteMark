package com.fomaxtro.notemark.presentation.screen.note_details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fomaxtro.notemark.R
import com.fomaxtro.notemark.presentation.designsystem.theme.NoteMarkTheme

@Composable
fun NoteMetadata(
    modifier: Modifier = Modifier,
    createdAt: String,
    lastEditedAt: String
) {
    Row(
        modifier = modifier
            .padding(
                horizontal = 16.dp,
                vertical = 20.dp
            )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = stringResource(R.string.date_created),
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = createdAt,
                style = MaterialTheme.typography.titleSmall
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = stringResource(R.string.last_edited),
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = lastEditedAt,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NoteMetadataPreview() {
    NoteMarkTheme {
        NoteMetadata(
            modifier = Modifier
                .fillMaxWidth(),
            createdAt = "26 Sep 2024, 18:54",
            lastEditedAt = "Just now"
        )
    }
}