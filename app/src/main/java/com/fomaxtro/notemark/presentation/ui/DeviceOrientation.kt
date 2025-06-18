package com.fomaxtro.notemark.presentation.ui

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.window.core.layout.WindowWidthSizeClass

enum class DeviceOrientation {
    PHONE_PORTRAIT,
    TABLET_PORTRAIT,
    PHONE_TABLET_LANDSCAPE
}

@Composable
fun rememberDeviceOrientation(): DeviceOrientation {
    val widthSizeClass = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass

    return remember(widthSizeClass) {
        when (widthSizeClass) {
            WindowWidthSizeClass.COMPACT -> DeviceOrientation.PHONE_PORTRAIT
            WindowWidthSizeClass.MEDIUM -> DeviceOrientation.TABLET_PORTRAIT
            WindowWidthSizeClass.EXPANDED -> DeviceOrientation.PHONE_TABLET_LANDSCAPE
            else -> DeviceOrientation.PHONE_PORTRAIT
        }
    }
}