package com.fomaxtro.notemark.presentation.designsystem.buttons

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fomaxtro.notemark.presentation.designsystem.theme.NoteMarkTheme
import com.fomaxtro.notemark.presentation.designsystem.theme.PrimaryOpacity10

@Composable
fun NoteMarkIconToggleButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    FilledTonalIconToggleButton(
        modifier = modifier,
        checked = checked,
        onCheckedChange = onCheckedChange,
        content = content,
        colors = IconButtonDefaults.filledTonalIconToggleButtonColors(
            checkedContainerColor = PrimaryOpacity10,
            checkedContentColor = MaterialTheme.colorScheme.primary,
            containerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(12.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun NoteMarkIconToggleButtonPreview() {
    NoteMarkTheme {
        NoteMarkIconToggleButton(
            checked = true,
            onCheckedChange = {}
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )
        }
    }
}