package com.fomaxtro.notemark.presentation.screen.edit_note.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fomaxtro.notemark.presentation.designsystem.app_bars.NoteMarkTopAppBarDefaults
import com.fomaxtro.notemark.presentation.ui.DeviceOrientation
import com.fomaxtro.notemark.presentation.ui.rememberDeviceOrientation

@Composable
fun AdaptiveScaffold(
    modifier: Modifier = Modifier,
    topAppBar: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    val deviceOrientation = rememberDeviceOrientation()

    Surface(
        modifier = modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.surfaceContainerLowest
    ) {
        if (deviceOrientation == DeviceOrientation.PHONE_TABLET_LANDSCAPE) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                topAppBar()

                Box(
                    modifier = Modifier
                        .width(540.dp)
                        .fillMaxHeight()
                        .align(Alignment.TopCenter)
                        .windowInsetsPadding(NoteMarkTopAppBarDefaults.windowInsets)
                        .consumeWindowInsets(NoteMarkTopAppBarDefaults.windowInsets)
                ) {
                    content()
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                topAppBar()
                content()
            }
        }
    }
}