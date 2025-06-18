package com.fomaxtro.notemark.presentation.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.fomaxtro.notemark.R

val SpaceGrotesk = FontFamily(
    Font(
        resId = R.font.spacegrotesk_bold,
        weight = FontWeight.Bold
    ),
    Font(
        resId = R.font.spacegrotesk_medium,
        weight = FontWeight.Medium
    )
)

val Inter = FontFamily(
    Font(
        resId = R.font.inter_medium,
        weight = FontWeight.Medium
    ),
    Font(
        resId = R.font.inter_regular,
        weight = FontWeight.Normal
    )
)

val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = SpaceGrotesk,
        fontSize = 36.sp,
        lineHeight = 40.sp,
        fontWeight = FontWeight.Bold
    ),
    titleMedium = TextStyle(
        fontFamily = SpaceGrotesk,
        fontSize = 32.sp,
        lineHeight = 36.sp,
        fontWeight = FontWeight.Bold
    ),
    titleSmall = TextStyle(
        fontFamily = SpaceGrotesk,
        fontSize = 17.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Medium,
    ),
    bodyLarge = TextStyle(
        fontFamily = Inter,
        fontSize = 17.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Normal
    ),
    bodyMedium = TextStyle(
        fontFamily = Inter,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Medium
    ),
    bodySmall = TextStyle(
        fontFamily = Inter,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Normal
    )
)