package com.fomaxtro.notemark.presentation.screen.landing

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fomaxtro.notemark.R
import com.fomaxtro.notemark.presentation.designsystem.buttons.NoteMarkButton
import com.fomaxtro.notemark.presentation.designsystem.buttons.NoteMarkOutlinedButton
import com.fomaxtro.notemark.presentation.designsystem.cards.NoteMarkSheet
import com.fomaxtro.notemark.presentation.designsystem.cards.NoteMarkSheetDefaults
import com.fomaxtro.notemark.presentation.designsystem.theme.NoteMarkTheme
import com.fomaxtro.notemark.presentation.ui.DeviceOrientation
import com.fomaxtro.notemark.presentation.ui.ObserveAsEvents
import com.fomaxtro.notemark.presentation.ui.rememberDeviceOrientation
import org.koin.androidx.compose.koinViewModel

@Composable
fun LandingRoot(
    navigateToRegistration: () -> Unit,
    navigateToLogin: () -> Unit,
    viewModel: LandingViewModel = koinViewModel()
) {
    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            LandingEvent.NavigateToLogin -> navigateToLogin()
            LandingEvent.NavigateToRegistration -> navigateToRegistration()
        }
    }

    LandingScreen(
        onAction = viewModel::onAction
    )
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
private fun LandingScreen(
    onAction: (LandingAction) -> Unit = {}
) {
    val deviceOrientation = rememberDeviceOrientation()
    var mainSheetHeight by rememberSaveable {
        mutableIntStateOf(Int.MAX_VALUE)
    }
    val sheetTopPadding = 32.dp
    val landingBackgroundColor = Color(0xFFE0EAFF)

    when (deviceOrientation) {
        DeviceOrientation.PHONE_PORTRAIT -> {
            BoxWithConstraints(
                modifier = Modifier.fillMaxSize()
            ) {
                LandingImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(
                            with(LocalDensity.current) {
                                (constraints.maxHeight - mainSheetHeight).toDp() + sheetTopPadding
                            }
                        )
                )

                MainSheet(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .onGloballyPositioned { coordinates ->
                            mainSheetHeight = coordinates.size.height
                        },
                    shape = NoteMarkSheetDefaults.shape,
                    contentPadding = PaddingValues(
                        top = 32.dp,
                        bottom = 40.dp,
                        start = 16.dp,
                        end = 16.dp
                    ),
                    onAction = onAction
                )
            }
        }
        DeviceOrientation.TABLET_PORTRAIT -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(landingBackgroundColor)
            ) {
                LandingImage(
                    modifier = Modifier
                        .offset(
                            y = (-88).dp
                        )
                        .fillMaxSize()
                )

                MainSheet(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 48.dp),
                    shape = NoteMarkSheetDefaults.shape,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(48.dp),
                    onAction = onAction
                )
            }
        }
        DeviceOrientation.PHONE_TABLET_LANDSCAPE -> {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(landingBackgroundColor),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LandingImage(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                )

                MainSheet(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(
                        topStart = 20.dp,
                        bottomStart = 20.dp
                    ),
                    contentPadding = PaddingValues(
                        top = 40.dp,
                        bottom = 40.dp,
                        start = 60.dp,
                        end = 40.dp
                    ),
                    onAction = onAction
                )
            }
        }
    }
}

@Composable
private fun LandingImage(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(R.drawable.landing),
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun MainSheet(
    onAction: (LandingAction) -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    contentPadding: PaddingValues
) {
    NoteMarkSheet(
        modifier = modifier,
        shape = shape
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding),
            horizontalAlignment = horizontalAlignment
        ) {
            Text(
                text = stringResource(R.string.landing_sheet_title),
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = stringResource(R.string.landing_sheet_subtitle),
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(
                modifier = Modifier.height(36.dp)
            )

            NoteMarkButton(
                onClick = {
                    onAction(LandingAction.OnGetStartedButtonClick)
                },
                text = stringResource(R.string.get_started),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            NoteMarkOutlinedButton(
                onClick = {
                    onAction(LandingAction.OnLogInButtonClick)
                },
                text = stringResource(R.string.log_in),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun LandingScreenPreview() {
    NoteMarkTheme {
        LandingScreen()
    }
}