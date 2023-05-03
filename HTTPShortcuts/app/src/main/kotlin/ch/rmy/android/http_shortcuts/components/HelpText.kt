package ch.rmy.android.http_shortcuts.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ch.rmy.android.http_shortcuts.R

@Composable
fun HelpText(
    text: String,
) {
    Text(
        text = text,
        fontSize = FontSize.MEDIUM,
    )
}