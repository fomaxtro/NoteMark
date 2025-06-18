package com.fomaxtro.notemark.presentation.designsystem.buttons

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import com.fomaxtro.notemark.presentation.designsystem.theme.NoteMarkTheme

@Composable
fun NoteMarkOutlinedButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        border = ButtonDefaults.outlinedButtonBorder().copy(
            brush = SolidColor(MaterialTheme.colorScheme.primary)
        ),
        shape = MaterialTheme.shapes.medium,
        contentPadding = NoteMarkButtonDefaults.contentPadding
    ) {
        Text(
            text = text,
            style = NoteMarkButtonDefaults.textStyle
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NoteMarkOutlinedButtonPreview() {
    NoteMarkTheme {
        NoteMarkOutlinedButton(
            onClick = {},
            text = "Label"
        )
    }
}