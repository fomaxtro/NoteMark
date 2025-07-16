package com.fomaxtro.notemark.presentation.screen.settings

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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fomaxtro.notemark.R
import com.fomaxtro.notemark.presentation.designsystem.app_bars.NoteMarkTopAppBar
import com.fomaxtro.notemark.presentation.designsystem.scaffolds.NoteMarkScaffold
import com.fomaxtro.notemark.presentation.designsystem.theme.NoteMarkTheme
import com.fomaxtro.notemark.presentation.screen.settings.components.SettingActionButton
import com.fomaxtro.notemark.presentation.screen.settings.components.SettingDropdownMenuItem
import com.fomaxtro.notemark.presentation.screen.settings.components.SettingListItem
import com.fomaxtro.notemark.presentation.ui.ObserveAsEvents
import com.fomaxtro.notemark.presentation.ui.rememberAdaptiveHorizontalPadding
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsRoot(
    navigateBack: () -> Unit,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            SettingsEvent.NavigateBack -> navigateBack()
        }
    }

    SettingsScreen(
        onAction = viewModel::onAction,
        state = state
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(
    onAction: (SettingsAction) -> Unit = {},
    state: SettingsState
) {
    val horizontalPadding = rememberAdaptiveHorizontalPadding()

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
        NoteMarkScaffold(
            topAppBar = {
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
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontalPadding)
            ) {
                SettingListItem(
                    icon = Icons.Default.AccessTime,
                    title = stringResource(R.string.sync_interval),
                    onClick = {},
                    action = {
                        SettingActionButton(
                            text = stringResource(R.string.manual_only),
                            onClick = {}
                        )

                        DropdownMenu(
                            expanded = false,
                            onDismissRequest = {},
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
                                onClick = {},
                                isSelected = true
                            )

                            SettingDropdownMenuItem(
                                text = stringResource(R.string.fifteen_minutes),
                                onClick = {},
                                isSelected = false
                            )

                            SettingDropdownMenuItem(
                                text = stringResource(R.string.thirty_minutes),
                                onClick = {},
                                isSelected = false
                            )

                            SettingDropdownMenuItem(
                                text = stringResource(R.string.one_hour),
                                onClick = {},
                                isSelected = false
                            )
                        }
                    }
                )

                HorizontalDivider()

                SettingListItem(
                    icon = Icons.Default.Sync,
                    title = stringResource(R.string.sync_data),
                    subtitle = stringResource(R.string.last_sync, "12 min ago"),
                    onClick = {}
                )

                HorizontalDivider()

                SettingListItem(
                    icon = ImageVector.vectorResource(R.drawable.log_out),
                    title = stringResource(R.string.log_out),
                    onClick = {},
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    NoteMarkTheme {
        SettingsScreen(
            state = SettingsState()
        )
    }
}