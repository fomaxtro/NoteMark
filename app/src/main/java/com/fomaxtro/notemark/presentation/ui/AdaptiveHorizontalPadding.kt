package com.fomaxtro.notemark.presentation.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp

@Composable
fun rememberAdaptiveHorizontalPadding(): PaddingValues {
    val deviceOrientation = rememberDeviceOrientation()

    return remember(deviceOrientation) {
        PaddingValues(
            start = if (deviceOrientation == DeviceOrientation.PHONE_TABLET_LANDSCAPE) {
                60.dp
            } else {
                16.dp
            },
            end = 16.dp
        )
    }
}