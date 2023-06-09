package ch.rmy.android.http_shortcuts.activities.widget

import android.app.Application
import android.graphics.Color
import ch.rmy.android.framework.viewmodel.BaseViewModel
import ch.rmy.android.http_shortcuts.dagger.getApplicationComponent
import ch.rmy.android.http_shortcuts.data.domains.shortcuts.ShortcutId
import ch.rmy.android.http_shortcuts.icons.ShortcutIcon

class WidgetSettingsViewModel(application: Application) :
    BaseViewModel<WidgetSettingsViewModel.InitData, WidgetSettingsViewState>(application) {

    init {
        getApplicationComponent().inject(this)
    }

    private val shortcutId: ShortcutId
        get() = initData.shortcutId
    private val shortcutName: String
        get() = initData.shortcutName
    private val shortcutIcon: ShortcutIcon
        get() = initData.shortcutIcon

    data class InitData(
        val shortcutId: ShortcutId,
        val shortcutName: String,
        val shortcutIcon: ShortcutIcon,
    )

    override fun initViewState() = WidgetSettingsViewState(
        showLabel = true,
        labelColor = Color.WHITE,
        shortcutIcon = shortcutIcon,
        shortcutName = shortcutName,
    )

    fun onLabelColorButtonClicked() {
        updateViewState {
            copy(colorDialogVisible = true)
        }
    }

    fun onShowLabelChanged(enabled: Boolean) {
        updateViewState {
            copy(showLabel = enabled)
        }
    }

    fun onLabelColorSelected(color: Int) {
        updateViewState {
            copy(
                colorDialogVisible = false,
                labelColor = color,
            )
        }
    }

    fun onCreateButtonClicked() {
        doWithViewState { viewState ->
            finishWithOkResult(
                WidgetSettingsActivity.OpenWidgetSettings.createResult(
                    shortcutId = shortcutId,
                    labelColor = viewState.labelColorFormatted,
                    showLabel = viewState.showLabel,
                ),
            )
        }
    }

    fun onDialogDismissalRequested() {
        updateViewState {
            copy(colorDialogVisible = false)
        }
    }
}
