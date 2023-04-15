package ch.rmy.android.http_shortcuts.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ch.rmy.android.http_shortcuts.R
import ch.rmy.android.http_shortcuts.data.domains.shortcuts.ShortcutId
import ch.rmy.android.http_shortcuts.scripting.shortcuts.ShortcutPlaceholder

@Composable
fun ShortcutPickerDialog(
    title: String,
    shortcuts: List<ShortcutPlaceholder>,
    includeCurrentShortcutOption: Boolean,
    onShortcutSelected: (ShortcutId) -> Unit,
    onCurrentShortcutSelected: () -> Unit,
    onDismissRequested: () -> Unit,
) {
    SelectDialog(
        title = title,
        onDismissRequest = onDismissRequested,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if (includeCurrentShortcutOption) {
                item(key = "current") {
                    MenuDialogEntry(
                        label = stringResource(R.string.label_insert_action_code_for_current_shortcut),
                        onClick = onCurrentShortcutSelected,
                    )
                }
            }
            items(
                items = shortcuts,
                key = { it.id },
            ) { shortcut ->
                MenuDialogEntry(
                    label = shortcut.name,
                    icon = shortcut.icon,
                    onClick = {
                        onShortcutSelected(shortcut.id)
                    }
                )
            }
        }
    }
}