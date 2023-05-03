package ch.rmy.android.http_shortcuts.activities.variables.editor

import androidx.compose.runtime.Composable
import ch.rmy.android.http_shortcuts.activities.variables.editor.types.ColorTypeViewState
import ch.rmy.android.http_shortcuts.activities.variables.editor.types.VariableTypeViewState
import ch.rmy.android.http_shortcuts.activities.variables.editor.types.ColorTypeEditor
import ch.rmy.android.http_shortcuts.activities.variables.editor.types.ConstantTypeEditor
import ch.rmy.android.http_shortcuts.activities.variables.editor.types.ConstantTypeViewState

@Composable
fun VariableTypeSpecificContent(
    viewState: VariableTypeViewState?,
    onViewStateChanged: (VariableTypeViewState) -> Unit,
) {
    when (viewState) {
        is ColorTypeViewState -> ColorTypeEditor(viewState, onViewStateChanged)
        is ConstantTypeViewState -> ConstantTypeEditor(viewState, onViewStateChanged)
        else -> Unit
    }
}

