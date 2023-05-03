package ch.rmy.android.http_shortcuts.activities.variables.editor.types

import androidx.compose.runtime.Stable
import ch.rmy.android.http_shortcuts.activities.variables.editor.types.VariableTypeViewState

@Stable
data class ColorTypeViewState(
    val rememberValue: Boolean,
) : VariableTypeViewState
