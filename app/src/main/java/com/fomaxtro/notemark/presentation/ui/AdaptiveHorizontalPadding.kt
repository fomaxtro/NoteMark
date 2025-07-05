package com.fomaxtro.notemark.presentation.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun rememberAdaptiveHorizontalPadding(): PaddingValues {
    val deviceOrientation = rememberDeviceOrientation()

    return if (deviceOrientation == DeviceOrientation.PHONE_TABLET_LANDSCAPE) {
        PaddingValues(
            start = 60.dp,
            end = 16.dp
        )
    } else {
        PaddingValues(
            horizontal = 16.dp
        )
    }
}