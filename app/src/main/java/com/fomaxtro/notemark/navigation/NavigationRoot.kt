package com.fomaxtro.notemark.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.fomaxtro.notemark.R
import com.fomaxtro.notemark.presentation.screen.edit_note.EditNoteRoot
import com.fomaxtro.notemark.presentation.screen.edit_note.mode.EditNoteMode
import com.fomaxtro.notemark.presentation.screen.landing.LandingRoot
import com.fomaxtro.notemark.presentation.screen.login.LoginRoot
import com.fomaxtro.notemark.presentation.screen.note_details.NoteDetailsRoot
import com.fomaxtro.notemark.presentation.screen.note_list.NoteListRoot
import com.fomaxtro.notemark.presentation.screen.registration.RegistrationRoot
import com.fomaxtro.notemark.presentation.screen.settings.SettingsRoot
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
                navController.navigate(Route.Login) {
                    launchSingleTop = true

                    popUpTo(0) {
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
        startDestination = if (isLoggedIn) Route.NoteList else Route.Landing
    ) {
        composable<Route.Landing> {
            LandingRoot(
                navigateToRegistration = {
                    navController.navigate(Route.Registration) {
                        launchSingleTop = true

                        popUpTo<Route.Landing> {
                            inclusive = true
                        }
                    }
                },
                navigateToLogin = {
                    navController.navigate(Route.Login) {
                        launchSingleTop = true

                        popUpTo<Route.Landing> {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<Route.Registration> {
            RegistrationRoot(
                navigateToLogin = {
                    navController.navigate(Route.Login) {
                        launchSingleTop = true

                        popUpTo<Route.Registration> {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<Route.Login> {
            LoginRoot(
                navigateToNoteList = {
                    navController.navigate(Route.NoteList) {
                        launchSingleTop = true

                        popUpTo<Route.Login> {
                            inclusive = true
                        }
                    }
                },
                navigateToRegistration = {
                    navController.navigate(Route.Registration) {
                        launchSingleTop = true

                        popUpTo<Route.Login> {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<Route.NoteList> {
            NoteListRoot(
                navigateToEditNote = { id ->
                    navController.navigate(Route.EditNote(id, EditNoteMode.CREATE)) {
                        launchSingleTop = true
                    }
                },
                navigateToSettings = {
                    navController.navigate(Route.Settings) {
                        launchSingleTop = true
                    }
                },
                navigateToNoteDetails = { id ->
                    navController.navigate(Route.NoteDetails(id)) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<Route.EditNote> {
            val arguments = it.toRoute<Route.EditNote>()

            EditNoteRoot(
                id = arguments.id,
                mode = arguments.mode,
                navigateBack = {
                    navController.navigateUp()
                }
            )
        }

        composable<Route.Settings> {
            SettingsRoot(
                navigateBack = {
                    navController.navigateUp()
                }
            )
        }

        composable<Route.NoteDetails> {
            val arguments = it.toRoute<Route.NoteDetails>()

            NoteDetailsRoot(
                id = arguments.id,
                navigateBack = {
                    navController.navigateUp()
                },
                navigateToEditNote = { id ->
                    navController.navigate(Route.EditNote(id, EditNoteMode.EDIT)) {
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}