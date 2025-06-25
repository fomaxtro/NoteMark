package com.fomaxtro.notemark

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fomaxtro.notemark.navigation.NavigationRoot
import com.fomaxtro.notemark.presentation.designsystem.theme.NoteMarkTheme
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition {
            viewModel.state.value.isCheckingAuth
        }
        enableEdgeToEdge()

        setContent {
            KoinAndroidContext {
                val state by viewModel.state.collectAsStateWithLifecycle()

                if (!state.isCheckingAuth) {
                    NoteMarkTheme {
                        NavigationRoot(
                            isLoggedIn = state.isLoggedIn
                        )
                    }
                }
            }
        }
    }
}
