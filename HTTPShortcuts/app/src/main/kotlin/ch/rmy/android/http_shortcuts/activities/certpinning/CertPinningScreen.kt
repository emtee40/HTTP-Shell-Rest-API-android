package ch.rmy.android.http_shortcuts.activities.certpinning

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ch.rmy.android.http_shortcuts.R
import ch.rmy.android.http_shortcuts.components.FloatingAddButton
import ch.rmy.android.http_shortcuts.components.ScreenScope
import ch.rmy.android.http_shortcuts.components.SimpleScaffold
import ch.rmy.android.http_shortcuts.components.ToolbarIcon
import ch.rmy.android.http_shortcuts.components.bindViewModel

@Composable
fun ScreenScope.CertPinningScreen() {
    val (viewModel, state) = bindViewModel<CertPinningViewState, CertPinningViewModel>()

    SimpleScaffold(
        viewState = state,
        title = stringResource(R.string.title_certificate_pinning),
        floatingActionButton = {
            FloatingAddButton(onClick = viewModel::onCreatePinButtonClicked)
        },
        actions = {
            ToolbarIcon(
                Icons.Filled.HelpOutline,
                contentDescription = stringResource(R.string.button_show_help),
                onClick = viewModel::onHelpButtonClicked,
            )
        }
    ) { viewState ->
        CertPinningContent(
            pins = viewState.pins,
            onPinClicked = viewModel::onPinClicked,
        )
    }

    CertPinningDialogs(
        dialogState = state?.dialogState,
        onEditConfirmed = viewModel::onEditConfirmed,
        onEditOptionSelected = viewModel::onEditOptionSelected,
        onDeleteOptionSelected = viewModel::onDeleteOptionSelected,
        onDeletionConfirmed = viewModel::onDeletionConfirmed,
        onDismissed = viewModel::onDialogDismissed,
    )
}
