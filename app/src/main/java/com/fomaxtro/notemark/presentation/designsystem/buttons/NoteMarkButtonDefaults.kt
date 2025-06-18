package com.fomaxtro.notemark.presentation.designsystem.buttons

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

object NoteMarkButtonDefaults {
    val contentPadding = PaddingValues(
        vertical = 10.dp,
        horizontal = 20.dp
    )
    val textStyle: TextStyle
        @Composable get() = MaterialTheme.typography.titleSmall
}