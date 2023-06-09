package ch.rmy.android.http_shortcuts.activities.importexport

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudDownload
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.Input
import androidx.compose.material.icons.outlined.Output
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import ch.rmy.android.framework.extensions.launch
import ch.rmy.android.http_shortcuts.R
import ch.rmy.android.http_shortcuts.activities.remote_edit.RemoteEditActivity
import ch.rmy.android.http_shortcuts.components.SettingsButton
import com.alorma.compose.settings.storage.base.rememberBooleanSettingState
import com.alorma.compose.settings.ui.SettingsGroup
import com.alorma.compose.settings.ui.SettingsSwitch

@Composable
fun ImportExportContent(
    useLegacyFormat: Boolean,
    onLegacyFormatUseChanged: (Boolean) -> Unit,
    onImportFromFileClicked: () -> Unit,
    onImportFromUrlClicked: () -> Unit,
    onExportClicked: () -> Unit,
    onRemoteEditorClosed: (changesImported: Boolean) -> Unit,
) {
    val openRemoteEdit = rememberLauncherForActivityResult(RemoteEditActivity.OpenRemoteEditor, onRemoteEditorClosed)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
    ) {
        SettingsGroup(
            title = { Text(stringResource(R.string.settings_title_import)) },
        ) {
            SettingsButton(
                icon = Icons.Outlined.Input,
                title = stringResource(R.string.settings_import_from_file),
                onClick = onImportFromFileClicked,
            )

            SettingsButton(
                icon = Icons.Outlined.CloudDownload,
                title = stringResource(R.string.settings_import_from_url),
                onClick = onImportFromUrlClicked,
            )
        }

        SettingsGroup(
            title = { Text(stringResource(R.string.settings_title_export)) },
        ) {
            SettingsButton(
                icon = Icons.Outlined.Output,
                title = stringResource(R.string.settings_export),
                onClick = onExportClicked,
            )

            SettingsSwitch(
                icon = {
                    Icon(
                        modifier = Modifier.alpha(0f),
                        imageVector = Icons.Outlined.Output,
                        contentDescription = null,
                    )
                },
                title = { Text(stringResource(R.string.settings_use_legacy_export_format)) },
                subtitle = { Text(stringResource(R.string.settings_use_legacy_export_format_summary)) },
                state = rememberBooleanSettingState(useLegacyFormat),
                onCheckedChange = onLegacyFormatUseChanged,
            )
        }

        SettingsGroup(
            title = { Text(stringResource(R.string.settings_title_remote_edit)) },
        ) {
            SettingsButton(
                icon = Icons.Outlined.Devices,
                title = stringResource(R.string.settings_remote_edit),
                onClick = {
                    openRemoteEdit.launch()
                },
            )
        }
    }
}
