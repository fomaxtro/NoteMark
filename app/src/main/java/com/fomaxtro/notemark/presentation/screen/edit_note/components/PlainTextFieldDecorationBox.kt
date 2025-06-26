package com.fomaxtro.notemark.presentation.screen.edit_note.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun PlainTextFieldDecorationBox(
    innerTextField: @Composable () -> Unit,
    placeHolder: String? = null,
    textStyle: TextStyle? = null,
    value: String
) {
    Box(
        modifier = Modifier
            .padding(
                horizontal = 16.dp,
                vertical = 20.dp
            )
    ) {
        if (value.isEmpty() && placeHolder != null) {
            Text(
                text = placeHolder,
                style = textStyle ?: LocalTextStyle.current
            )
        }

        innerTextField()
    }
}