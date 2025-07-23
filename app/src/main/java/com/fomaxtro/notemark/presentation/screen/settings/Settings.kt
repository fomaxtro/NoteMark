package com.fomaxtro.notemark.presentation.screen.settings

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fomaxtro.notemark.R
import com.fomaxtro.notemark.domain.model.SyncInterval
import com.fomaxtro.notemark.presentation.designsystem.app_bars.NoteMarkTopAppBar
import com.fomaxtro.notemark.presentation.designsystem.theme.NoteMarkTheme
import com.fomaxtro.notemark.presentation.screen.settings.components.SettingActionButton
import com.fomaxtro.notemark.presentation.screen.settings.components.SettingDropdownMenuItem
import com.fomaxtro.notemark.presentation.screen.settings.components.SettingListItem
import com.fomaxtro.notemark.presentation.ui.ObserveAsEvents
import com.fomaxtro.notemark.presentation.ui.rememberAdaptiveHorizontalPadding
import com.fomaxtro.notemark.presentation.util.toSyncDateTimeUiText
import org.koin.androidx.compose.koinViewModel
import java.time.Instant
import java.time.temporal.ChronoUnit

@Composable
fun SettingsRoot(
    navigateBack: () -> Unit,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            SettingsEvent.NavigateBack -> navigateBack()
            is SettingsEvent.ShoSystemMessage -> {
                Toast.makeText(
                    context,
                    event.message.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }
            is SettingsEvent.ShowMessage -> {
                snackbarHostState.showSnackbar(event.message.asString(context))
            }
        }
    }

    SettingsScreen(
        onAction = viewModel::onAction,
        state = state,
        snackbarHostState = snackbarHostState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(
    onAction: (SettingsAction) -> Unit = {},
    state: SettingsState,
    snackbarHostState: SnackbarHostState = SnackbarHostState()
) {
    val horizontalPadding = rememberAdaptiveHorizontalPadding()
    var isExpanded by remember {
        mutableStateOf(false)
    }

    if (state.isLoggingOut) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize()
            )
        }
    } else {
        Scaffold(
            topBar = {
                NoteMarkTopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.settings),
                            style = MaterialTheme.typography.titleSmall
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                onAction(SettingsAction.OnNavigateBackClick)
                            }
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.chevron_left),
                                contentDescription = stringResource(R.string.navigate_back)
                            )
                        }
                    }
                )
            },
            snackbarHost = {
                SnackbarHost(snackbarHostState)
            },
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontalPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val syncIntervalEnabled = state.hasInternetConnection && !state.isSyncing

                    SettingListItem(
                        icon = Icons.Default.AccessTime,
                        title = stringResource(R.string.sync_interval),
                        enabled = syncIntervalEnabled,
                        action = {
                            SettingActionButton(
                                text = when (state.syncInterval) {
                                    SyncInterval.MANUAL_ONLY -> {
                                        stringResource(R.string.manual_only)
                                    }
                                    SyncInterval.FIFTEEN_MINUTES -> {
                                        stringResource(R.string.fifteen_minutes)
                                    }
                                    SyncInterval.THIRTY_MINUTES -> {
                                        stringResource(R.string.thirty_minutes)
                                    }
                                    SyncInterval.ONE_HOUR -> {
                                        stringResource(R.string.one_hour)
                                    }
                                },
                                onClick = {
                                    isExpanded = true
                                },
                                enabled = syncIntervalEnabled
                            )

                            DropdownMenu(
                                expanded = isExpanded,
                                onDismissRequest = {
                                    isExpanded = false
                                },
                                shape = RoundedCornerShape(16.dp),
                                offset = DpOffset(
                                    x = 0.dp,
                                    y = 16.dp
                                ),
                                modifier = Modifier
                                    .width(190.dp),
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                            ) {
                                SettingDropdownMenuItem(
                                    text = stringResource(R.string.manual_only),
                                    onClick = {
                                        isExpanded = false

                                        onAction(SettingsAction.OnSyncIntervalChange(
                                            SyncInterval.MANUAL_ONLY
                                        ))
                                    },
                                    isSelected = state.syncInterval == SyncInterval.MANUAL_ONLY
                                )

                                SettingDropdownMenuItem(
                                    text = stringResource(R.string.fifteen_minutes),
                                    onClick = {
                                        isExpanded = false

                                        onAction(SettingsAction.OnSyncIntervalChange(
                                            SyncInterval.FIFTEEN_MINUTES
                                        ))
                                    },
                                    isSelected = state.syncInterval == SyncInterval.FIFTEEN_MINUTES
                                )

                                SettingDropdownMenuItem(
                                    text = stringResource(R.string.thirty_minutes),
                                    onClick = {
                                        isExpanded = false

                                        onAction(SettingsAction.OnSyncIntervalChange(
                                            SyncInterval.THIRTY_MINUTES
                                        ))
                                    },
                                    isSelected = state.syncInterval == SyncInterval.THIRTY_MINUTES
                                )

                                SettingDropdownMenuItem(
                                    text = stringResource(R.string.one_hour),
                                    onClick = {
                                        isExpanded = false

                                        onAction(SettingsAction.OnSyncIntervalChange(
                                            SyncInterval.ONE_HOUR
                                        ))
                                    },
                                    isSelected = state.syncInterval == SyncInterval.ONE_HOUR
                                )
                            }
                        }
                    )

                    HorizontalDivider()

                    SettingListItem(
                        icon = Icons.Default.Sync,
                        title = stringResource(R.string.sync_data),
                        subtitle = stringResource(
                            id = R.string.last_sync,
                            state.lastSyncTime.asString()
                        ),
                        onClick = {
                            onAction(SettingsAction.OnSyncDataClick)
                        },
                        enabled = !state.isSyncing
                    )

                    HorizontalDivider()

                    SettingListItem(
                        icon = ImageVector.vectorResource(R.drawable.log_out),
                        title = stringResource(R.string.log_out),
                        onClick = {
                            onAction(SettingsAction.OnLogoutClick)
                        },
                        color = MaterialTheme.colorScheme.error,
                        enabled = !state.isSyncing
                    )
                }

                if (state.isSyncing) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    val lastSyncDate = Instant
        .now()
        .minus(10, ChronoUnit.DAYS)

    NoteMarkTheme {
        SettingsScreen(
            state = SettingsState(
                lastSyncTime = lastSyncDate.toSyncDateTimeUiText()
            )
        )
    }
}