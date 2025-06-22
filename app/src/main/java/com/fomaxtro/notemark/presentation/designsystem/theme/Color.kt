package com.fomaxtro.notemark.presentation.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Primary = Color(0xFF5977F7)
val PrimaryOpacity10 = Primary.copy(alpha = 0.1f)
val OnPrimary = Color(0xFFFFFFFF)
val OnPrimaryOpacity12 = OnPrimary.copy(alpha = 0.12f)
val Surface = Color(0xFFEFEFF2)
val OnSurface = Color(0xFF1B1B1C)
val OnSurfaceOpacity12 = OnSurface.copy(alpha = 0.12f)
val OnSurfaceVariant = Color(0xFF535364)
val SurfaceLowest = Color(0xFFFFFFFF)
val Error = Color(0xFFE1294B)

val ColorScheme.primaryGradient: Brush
    get() = Brush.linearGradient(
        listOf(
            Color(0xFF58A1F8),
            Color(0xFF5A4CF7)
        )
    )