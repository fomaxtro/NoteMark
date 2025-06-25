package com.fomaxtro.notemark.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.fomaxtro.notemark.R
import com.fomaxtro.notemark.navigation.route.EditNote
import com.fomaxtro.notemark.navigation.route.Landing
import com.fomaxtro.notemark.navigation.route.Login
import com.fomaxtro.notemark.navigation.route.NoteList
import com.fomaxtro.notemark.navigation.route.Registration
import com.fomaxtro.notemark.presentation.screen.edit_note.EditNoteRoot
import com.fomaxtro.notemark.presentation.screen.landing.LandingRoot
import com.fomaxtro.notemark.presentation.screen.login.LoginRoot
import com.fomaxtro.notemark.presentation.screen.note_list.NoteListRoot
import com.fomaxtro.notemark.presentation.screen.registration.RegistrationRoot
import com.fomaxtro.notemark.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavigationRoot(
    isLoggedIn: Boolean,
    viewModel: NavigationViewModel = koinViewModel()
) {
    val navController = rememberNavController()
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            NavigationEvent.Logout -> {
                navController.navigate(Login) {
                    launchSingleTop = true

                    popUpTo(navController.graph.findStartDestination().id) {
                        inclusive = true
                    }
                }

                Toast.makeText(
                    context,
                    context.getText(R.string.user_log_out),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) NoteList else Landing
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
                navigateToNoteList = {
                    navController.navigate(NoteList) {
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

        composable<NoteList> {
            NoteListRoot(
                navigateToEditNote = {
                    navController.navigate(EditNote()) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<EditNote> {
            val arguments = it.toRoute<EditNote>()

            EditNoteRoot(
                id = arguments.id
            )
        }
    }
}