package com.fomaxtro.notemark.presentation.designsystem.app_bars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fomaxtro.notemark.presentation.designsystem.theme.NoteMarkTheme
import com.fomaxtro.notemark.presentation.screen.note_list.components.Avatar
import com.fomaxtro.notemark.presentation.ui.DeviceOrientation
import com.fomaxtro.notemark.presentation.ui.rememberDeviceOrientation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteMarkTopAppBar(
    title: @Composable () -> Unit,
    action: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    val deviceOrientation = rememberDeviceOrientation()

    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceContainerLowest
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
                .windowInsetsPadding(TopAppBarDefaults.windowInsets)
                .height(TopAppBarDefaults.TopAppBarExpandedHeight),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.titleMedium.copy(
                    fontSize = 20.sp
                )
            ) {
                Box(
                    modifier = Modifier
                        .then(
                            if (deviceOrientation == DeviceOrientation.PHONE_TABLET_LANDSCAPE) {
                                Modifier.padding(start = 60.dp)
                            } else Modifier
                        )
                ) {
                    title()
                }
            }

            action()
        }
    }
}

@Preview
@Composable
private fun NoteMarkTopAppBarPreview() {
    NoteMarkTheme {
        NoteMarkTopAppBar(
            title = {
                Text("NoteMark")
            },
            action = {
                Avatar(
                    name = "Foo"
                )
            }
        )
    }
}