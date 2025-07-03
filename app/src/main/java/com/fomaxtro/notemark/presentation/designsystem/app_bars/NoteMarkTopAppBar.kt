package com.fomaxtro.notemark.presentation.designsystem.app_bars

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.fomaxtro.notemark.presentation.designsystem.theme.NoteMarkTheme
import com.fomaxtro.notemark.presentation.screen.note_list.components.Avatar

@Composable
fun NoteMarkTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    action: @Composable () -> Unit = {},
    navigationIcon: @Composable () -> Unit = {},
    contentPadding: PaddingValues = PaddingValues()
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceContainerLowest
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(NoteMarkTopAppBarDefaults.padding)
                .padding(contentPadding)
                .windowInsetsPadding(NoteMarkTopAppBarDefaults.windowInsets)
                .height(NoteMarkTopAppBarDefaults.height),
            verticalAlignment = Alignment.CenterVertically
        ) {
            navigationIcon()
            
            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.titleMedium.copy(
                    fontSize = 20.sp
                )
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
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
            },
            navigationIcon = {
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                        contentDescription = null
                    )
                }
            }
        )
    }
}