package com.fomaxtro.notemark.presentation.designsystem.app_bars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.CloudOff
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fomaxtro.notemark.R
import com.fomaxtro.notemark.presentation.designsystem.theme.NoteMarkTheme
import com.fomaxtro.notemark.presentation.screen.note_list.components.Avatar
import com.fomaxtro.notemark.presentation.ui.rememberAdaptiveHorizontalPadding

@Composable
fun NoteMarkTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    action: @Composable () -> Unit = {},
    navigationIcon: (@Composable () -> Unit)? = null,
    hasInternet: Boolean = true
) {
    val contentPadding = rememberAdaptiveHorizontalPadding()

    val fixedContentPadding = if (navigationIcon != null) {
        PaddingValues(
            start = contentPadding.calculateStartPadding(LayoutDirection.Ltr) - 16.dp,
            end = contentPadding.calculateEndPadding(LayoutDirection.Ltr)
        )
    } else contentPadding

    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceContainerLowest
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(NoteMarkTopAppBarDefaults.padding)
                .padding(fixedContentPadding)
                .height(NoteMarkTopAppBarDefaults.height),
            verticalAlignment = Alignment.CenterVertically
        ) {
            navigationIcon?.invoke()
            
            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.titleMedium.copy(
                    fontSize = 20.sp
                )
            ) {
                title()

                if (!hasInternet) {
                    Spacer(modifier = Modifier.width(8.dp))

                    Icon(
                        imageVector = Icons.Default.CloudOff,
                        contentDescription = stringResource(R.string.no_internet),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .size(16.dp)
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement
                    .spacedBy(8.dp, Alignment.End),
                modifier = Modifier
                    .weight(1f),
            ) {
                action()
            }
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
            },
            hasInternet = false
        )
    }
}