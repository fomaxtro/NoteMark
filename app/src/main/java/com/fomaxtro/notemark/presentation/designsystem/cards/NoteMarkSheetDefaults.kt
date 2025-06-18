package com.fomaxtro.notemark.presentation.designsystem.cards

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

object NoteMarkSheetDefaults {
    val shape = RoundedCornerShape(
        topStart = 20.dp,
        topEnd = 20.dp
    )
    val colors: CardColors
        @Composable get() = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        )
}