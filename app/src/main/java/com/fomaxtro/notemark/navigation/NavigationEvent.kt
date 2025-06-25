package com.fomaxtro.notemark.navigation

sealed interface NavigationEvent {
    data object Logout : NavigationEvent
}