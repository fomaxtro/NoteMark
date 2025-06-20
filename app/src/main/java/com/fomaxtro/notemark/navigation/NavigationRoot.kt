package com.fomaxtro.notemark.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fomaxtro.notemark.presentation.screen.landing.LandingRoot
import com.fomaxtro.notemark.presentation.screen.login.LoginRoot
import com.fomaxtro.notemark.presentation.screen.registration.RegistrationRoot

@Composable
fun NavigationRoot(
    isLoggedIn: Boolean
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Home else Landing
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

                        popUpTo<Registration> {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<Login> {
            LoginRoot(
                navigateToHome = {
                    navController.navigate(Home) {
                        launchSingleTop = true

                        popUpTo<Login> {
                            inclusive = true
                        }
                    }
                },
                navigateToRegistration = {
                    navController.navigate(Registration) {
                        launchSingleTop = true

                        popUpTo<Login> {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<Home> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Blank Screen")
            }
        }
    }
}