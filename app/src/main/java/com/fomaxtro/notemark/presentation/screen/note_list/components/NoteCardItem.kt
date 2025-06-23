package com.fomaxtro.notemark.presentation.screen.note_list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fomaxtro.notemark.presentation.designsystem.theme.NoteMarkTheme
import com.fomaxtro.notemark.presentation.ui.DeviceOrientation
import com.fomaxtro.notemark.presentation.ui.rememberDeviceOrientation

@Composable
fun NoteCardItem(
    modifier: Modifier = Modifier,
    date: String,
    title: String,
    content: String
) {
    val deviceOrientation = rememberDeviceOrientation()

    val maxLength = if (deviceOrientation == DeviceOrientation.PHONE_PORTRAIT) 150 else 250
    val contentText = if (content.length > maxLength) {
        content.take(maxLength) + "..."
    } else {
        content
    }

    ElevatedCard(
        modifier = modifier
            .width(IntrinsicSize.Max)
            .height(IntrinsicSize.Max),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = date,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 20.sp
            )

            Text(
                text = contentText,
                style = MaterialTheme.typography.bodySmall,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview
@Composable
private fun NoteCardItemPreview() {
    NoteMarkTheme {
        NoteCardItem(
            date = "23 Jan 2024",
            title = "Title",
            content = "Augue non mauris ante viverra ut arcu sed ut lectus interdum morbi sed leo purus gravida non id mi augue."
        )
    }
}