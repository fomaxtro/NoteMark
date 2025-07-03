package com.fomaxtro.notemark.presentation.designsystem.app_bars

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
object NoteMarkTopAppBarDefaults {
    val padding = PaddingValues(
        vertical = 8.dp
    )
    val windowInsets: WindowInsets
        @Composable get() = TopAppBarDefaults.windowInsets
    val height = TopAppBarDefaults.TopAppBarExpandedHeight
}