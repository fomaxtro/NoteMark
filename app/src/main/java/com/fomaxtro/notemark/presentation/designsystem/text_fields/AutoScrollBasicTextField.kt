package com.fomaxtro.notemark.presentation.designsystem.text_fields

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldDecorator
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.fomaxtro.notemark.R
import com.fomaxtro.notemark.presentation.designsystem.theme.NoteMarkTheme
import com.fomaxtro.notemark.presentation.screen.edit_note.components.PlainTextFieldDecorationBox
import kotlin.math.roundToInt

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AutoScrolledBasicTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle.Default,
    decorator: TextFieldDecorator? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    val scrollState = rememberScrollState()
    var innerTextFieldHeight by remember {
        mutableIntStateOf(0)
    }
    var textLayoutResult by remember {
        mutableStateOf<TextLayoutResult?>(null)
    }
    val isImeVisible = WindowInsets.isImeVisible

    LaunchedEffect(isImeVisible) {
        if (isImeVisible && textLayoutResult != null) {
            val cursorRect = textLayoutResult?.getCursorRect(state.selection.end)
                ?: return@LaunchedEffect

            val cursorPosition = cursorRect.bottom
            val texLayoutHeight = textLayoutResult?.size?.height ?: return@LaunchedEffect

            val scrollableArea = texLayoutHeight - innerTextFieldHeight
            val scrollDelta = cursorPosition - innerTextFieldHeight

            if (scrollDelta.roundToInt() > 0) {
                val percentageScrollDelta = scrollDelta / scrollableArea
                val targetScroll = scrollState.maxValue * percentageScrollDelta

                scrollState.scrollTo(targetScroll.roundToInt())
            }
        }
    }

    BasicTextField(
        state = state,
        textStyle = textStyle,
        modifier = modifier,
        decorator = { innerTextField ->
            decorator?.Decoration {
                Box(
                    modifier = Modifier
                        .onSizeChanged { size ->
                            innerTextFieldHeight = size.height
                        }
                ) {
                    innerTextField()
                }
            }
        },
        onTextLayout = { textLayout ->
            textLayoutResult = textLayout()
        },
        cursorBrush = SolidColor(
            MaterialTheme.colorScheme.primary
        ),
        keyboardOptions = keyboardOptions
    )
}

@Preview(showBackground = true)
@Composable
private fun AutoScrolledBasicTextPreview() {
    var state by remember {
        mutableStateOf(
            TextFieldState(
                "Lorem ipsum dolor sit amet consectetur adipiscing elit. Quisque faucibus ex sapien vitae pellentesque sem placerat. In id cursus mi pretium tellus duis convallis. Tempus leo eu aenean sed diam urna tempor. Pulvinar vivamus fringilla lacus nec metus bibendum egestas. Iaculis massa nisl malesuada lacinia integer nunc posuere. Ut hendrerit semper vel class aptent taciti sociosqu. Ad litora torquent per conubia nostra inceptos himenaeos.\n" +
                        "\n" +
                        "Lorem ipsum dolor sit amet consectetur adipiscing elit. Quisque faucibus ex sapien vitae pellentesque sem placerat. In id cursus mi pretium tellus duis convallis. Tempus leo eu aenean sed diam urna tempor. Pulvinar vivamus fringilla lacus nec metus bibendum egestas. Iaculis massa nisl malesuada lacinia integer nunc posuere. Ut hendrerit semper vel class aptent taciti sociosqu. Ad litora torquent per conubia nostra inceptos himenaeos.\n" +
                        "\n" +
                        "Lorem ipsum dolor sit amet consectetur adipiscing elit. Quisque faucibus ex sapien vitae pellentesque sem placerat. In id cursus mi pretium tellus duis convallis. Tempus leo eu aenean sed diam urna tempor. Pulvinar vivamus fringilla lacus nec metus bibendum egestas. Iaculis massa nisl malesuada lacinia integer nunc posuere. Ut hendrerit semper vel class aptent taciti sociosqu. Ad litora torquent per conubia nostra inceptos himenaeos.\n" +
                        "\n" +
                        "Lorem ipsum dolor sit amet consectetur adipiscing elit. Quisque faucibus ex sapien vitae pellentesque sem placerat. In id cursus mi pretium tellus duis convallis. Tempus leo eu aenean sed diam urna tempor. Pulvinar vivamus fringilla lacus nec metus bibendum egestas. Iaculis massa nisl malesuada lacinia integer nunc posuere. Ut hendrerit semper vel class aptent taciti sociosqu. Ad litora torquent per conubia nostra inceptos himenaeos.\n" +
                        "\n" +
                        "Lorem ipsum dolor sit amet consectetur adipiscing elit. Quisque faucibus ex sapien vitae pellentesque sem placerat. In id cursus mi pretium tellus duis convallis. Tempus leo eu aenean sed diam urna tempor. Pulvinar vivamus fringilla lacus nec metus bibendum egestas. Iaculis massa nisl malesuada lacinia integer nunc posuere. Ut hendrerit semper vel class aptent taciti sociosqu. Ad litora torquent per conubia nostra inceptos himenaeos."
            )
        )
    }

    val contentTextStyle = MaterialTheme.typography.bodyLarge.copy(
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    NoteMarkTheme {
        AutoScrolledBasicTextField(
            state = state,
            decorator = { innerTextField ->
                PlainTextFieldDecorationBox(
                    value = state.text.toString(),
                    innerTextField = innerTextField,
                    placeholder = stringResource(R.string.note_content_placeholder),
                    textStyle = contentTextStyle
                )
            },
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
            textStyle = contentTextStyle
        )
    }
}