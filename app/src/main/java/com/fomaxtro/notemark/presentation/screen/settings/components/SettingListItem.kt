package com.fomaxtro.notemark.presentation.screen.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fomaxtro.notemark.presentation.designsystem.theme.NoteMarkTheme
import com.fomaxtro.notemark.presentation.designsystem.theme.disabled

@Composable
fun SettingListItem(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit,
    action: @Composable () -> Unit = {},
    subtitle: String? = null,
    color: Color = LocalContentColor.current,
    enabled: Boolean = true
) {
    Row(
        modifier = modifier
            .clickable(
                onClick = onClick,
                enabled = enabled
            )
            .padding(
                vertical = 16.dp
            )
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CompositionLocalProvider(
            LocalContentColor provides if (enabled) {
                color
            } else {
                MaterialTheme.colorScheme.disabled
            }
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null
            )

            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall
                )

                subtitle?.let { subtitle ->
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                action()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingButtonPreview() {
    NoteMarkTheme {
        SettingListItem(
            modifier = Modifier
                .fillMaxWidth(),
            icon = Icons.Default.Sync,
            title = "Sync Data",
            subtitle = "Last sync: 12:00 AM",
            action = {
                SettingActionButton(
                    text = "Manual only",
                    onClick = {}
                )
            },
            onClick = {}
        )
    }
}