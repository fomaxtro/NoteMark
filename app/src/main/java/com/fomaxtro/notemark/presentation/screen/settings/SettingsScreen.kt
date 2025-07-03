package com.fomaxtro.notemark.presentation.screen.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fomaxtro.notemark.R
import com.fomaxtro.notemark.presentation.designsystem.app_bars.NoteMarkTopAppBar
import com.fomaxtro.notemark.presentation.designsystem.theme.NoteMarkTheme
import com.fomaxtro.notemark.presentation.ui.rememberAdaptiveHorizontalPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen() {
    val contentPadding = rememberAdaptiveHorizontalPadding()

    Scaffold(
        topBar = {
            NoteMarkTopAppBar(
                title = {
                    Text(stringResource(R.string.settings))
                },
                navigationIcon = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.chevron_left),
                            contentDescription = stringResource(R.string.navigate_back)
                        )
                    }
                },
                contentPadding = contentPadding
            )
        },
        containerColor = Color.White
    ) { innerTextField ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerTextField)
                .padding(top = 16.dp)
                .padding(contentPadding)
        ) {
            TextButton(
                onClick = {},
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.log_out),
                    contentDescription = stringResource(R.string.log_out)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = stringResource(R.string.log_out),
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    NoteMarkTheme {
        SettingsScreen()
    }
}