package com.fomaxtro.notemark.presentation.designsystem.sheets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fomaxtro.notemark.presentation.designsystem.cards.NoteMarkSheet
import com.fomaxtro.notemark.presentation.designsystem.theme.NoteMarkTheme
import com.fomaxtro.notemark.presentation.ui.DeviceOrientation
import com.fomaxtro.notemark.presentation.ui.rememberDeviceOrientation

@Composable
fun AdaptiveTwoPaneSheet(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    content: @Composable () -> Unit
) {
    val deviceOrientation = rememberDeviceOrientation()
    val contentSpacing = 32.dp

    NoteMarkSheet(
        modifier = modifier
    ) {
        when (deviceOrientation) {
            DeviceOrientation.PHONE_PORTRAIT -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .padding(
                            top = 32.dp,
                            bottom = 40.dp
                        )
                        .verticalScroll(rememberScrollState())
                ) {
                    HeaderPane(
                        title = title,
                        subtitle = subtitle
                    )

                    Spacer(modifier = Modifier.height(contentSpacing))

                    content()
                }
            }

            DeviceOrientation.TABLET_PORTRAIT -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            horizontal = 120.dp,
                            vertical = 100.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HeaderPane(
                        title = title,
                        subtitle = subtitle
                    )

                    Spacer(modifier = Modifier.height(contentSpacing))

                    content()
                }
            }

            DeviceOrientation.PHONE_TABLET_LANDSCAPE -> {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = 32.dp,
                            bottom = 40.dp,
                            start = 60.dp,
                            end = 40.dp
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                    ) {
                        HeaderPane(
                            title = title,
                            subtitle = subtitle
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .imePadding()
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        content()
                    }
                }
            }
        }
    }
}

@Composable
private fun HeaderPane(
    title: String,
    subtitle: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF252525
)
@Composable
private fun AdaptiveTwoPaneSheetPreview() {
    NoteMarkTheme {
        AdaptiveTwoPaneSheet(
            modifier = Modifier
                .fillMaxSize(),
            title = "Title",
            subtitle = "Subtitle"
        ) {
            Text(text = "Content")
        }
    }
}
