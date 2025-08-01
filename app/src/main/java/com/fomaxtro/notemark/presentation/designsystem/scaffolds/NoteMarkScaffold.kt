package com.fomaxtro.notemark.presentation.designsystem.scaffolds

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fomaxtro.notemark.presentation.designsystem.app_bars.NoteMarkTopAppBar

private enum class NoteMarkScaffoldSlot {
    TOP_APP_BAR,
    CONTENT
}

@Composable
fun NoteMarkScaffold(
    modifier: Modifier = Modifier,
    topAppBar: @Composable () -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerLowest,
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: NoteMarkFabPosition = NoteMarkFabPosition.END,
    content: @Composable (innerPadding: PaddingValues) -> Unit,
) {
    val density = LocalDensity.current

    Surface(
        modifier = modifier
            .fillMaxSize(),
        color = containerColor
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            SubcomposeLayout(
                modifier = Modifier
                    .fillMaxSize()
            ) { constraints ->
                val topAppBarPlaceable = subcompose(NoteMarkScaffoldSlot.TOP_APP_BAR, topAppBar)
                    .firstOrNull()
                    ?.measure(
                        constraints.copy(
                            minHeight = 0
                        )
                    )

                val contentMeasurable = subcompose(NoteMarkScaffoldSlot.CONTENT) {
                    Box(
                        modifier = Modifier.navigationBarsPadding()
                    ) {
                        content(
                            PaddingValues(
                                top = with(density) {
                                    topAppBarPlaceable?.height?.toDp() ?: 0.dp
                                }
                            )
                        )
                    }
                }

                val contentPlaceable = contentMeasurable
                    .firstOrNull()
                    ?.measure(
                        constraints.copy(
                            minWidth = 0
                        )
                    )

                layout(constraints.maxWidth, constraints.maxHeight) {
                    topAppBarPlaceable?.place(0, 0)

                    contentPlaceable?.let { placeable ->
                        placeable.place(
                            x = (constraints.maxWidth - placeable.width) / 2,
                            y = 0
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .align(
                        when (floatingActionButtonPosition) {
                            NoteMarkFabPosition.END -> Alignment.BottomEnd
                            NoteMarkFabPosition.CENTER -> Alignment.BottomCenter
                        }
                    )
                    .padding(16.dp)
                    .systemBarsPadding()
            ) {
                floatingActionButton()
            }
        }
    }
}

@Preview
@Composable
private fun NoteMarkScaffoldPreview() {
    NoteMarkScaffold(
        topAppBar = {
            NoteMarkTopAppBar(
                title = {
                    Text("This is a text")
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(30) {
                Text("Item $it")
            }
        }
    }
}