package com.fomaxtro.notemark.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fomaxtro.notemark.presentation.screen.landing.LandingRoot
import com.fomaxtro.notemark.presentation.screen.login.LoginRoot
import com.fomaxtro.notemark.presentation.screen.registration.RegistrationRoot

@Composable
fun NavigationRoot() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Landing
    ) {
        composable<Landing> {
            LandingRoot(
                navigateToRegistration = {
                    navController.navigate(Registration) {
                        launchSingleTop = true

                        popUpTo<Landing> {
                            inclusive = true
                        }
                    }
                },
                navigateToLogin = {
                    navController.navigate(Login) {
                        launchSingleTop = true

                        popUpTo<Landing> {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<Registration> {
            RegistrationRoot(
                navigateToLogin = {
                    navController.navigate(Login) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<Login> {
            LoginRoot(
                navigateToHome = {},
                navigateToRegistration = {}
            )
        }
    }
}