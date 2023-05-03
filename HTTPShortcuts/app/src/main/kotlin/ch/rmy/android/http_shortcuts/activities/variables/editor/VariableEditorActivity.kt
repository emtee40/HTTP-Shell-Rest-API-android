package ch.rmy.android.http_shortcuts.activities.variables.editor

import androidx.compose.runtime.Composable
import ch.rmy.android.framework.ui.BaseIntentBuilder
import ch.rmy.android.http_shortcuts.activities.BaseComposeActivity
import ch.rmy.android.http_shortcuts.components.ScreenScope
import ch.rmy.android.http_shortcuts.data.domains.variables.VariableId
import ch.rmy.android.http_shortcuts.data.enums.VariableType

class VariableEditorActivity : BaseComposeActivity() {

    @Composable
    override fun ScreenScope.Content() {
        VariableEditorScreen(
            variableId = intent.getStringExtra(EXTRA_VARIABLE_ID),
            variableType = VariableType.parse(intent.getStringExtra(EXTRA_VARIABLE_TYPE)),
        )
    }

    /*
    private fun initUserInputBindings() {
        binding.inputVariableKey.doOnTextChanged { text ->
            viewModel.onVariableKeyChanged(text.toString())
        }
        binding.inputVariableTitle.doOnTextChanged { text ->
            viewModel.onVariableTitleChanged(text.toString())
        }
        binding.inputVariableMessage.doOnTextChanged { text ->
            viewModel.onVariableMessageChanged(text.toString())
        }

        binding.inputUrlEncode.doOnCheckedChanged(viewModel::onUrlEncodeChanged)
        binding.inputJsonEncode.doOnCheckedChanged(viewModel::onJsonEncodeChanged)
        binding.inputAllowShare.doOnCheckedChanged(viewModel::onAllowShareChanged)

        lifecycleScope.launch {
            binding.inputShareSupport.selectionChanges.collect { selectedOption ->
                viewModel.onShareSupportChanged(VariableEditorViewState.ShareSupport.valueOf(selectedOption))
            }
        }
    }

    private fun initViewModelBindings() {
        collectViewStateWhileActive(viewModel) { viewState ->
            binding.loadingIndicator.isVisible = false
            binding.mainView.isVisible = true
            setTitle(viewState.title)
            setSubtitle(viewState.subtitle)
            binding.dialogTitleContainer.isVisible = viewState.dialogTitleVisible
            binding.dialogMessageContainer.isVisible = viewState.dialogMessageVisible
            binding.inputVariableKey.error = viewState.variableKeyInputError?.localize(context)
            binding.inputVariableKey.setTextSafely(viewState.variableKey)
            binding.inputVariableTitle.setTextSafely(viewState.variableTitle)
            binding.inputVariableMessage.setTextSafely(viewState.variableMessage)
            if (viewState.variableKeyErrorHighlighting) {
                binding.inputVariableKey.setTextColor(Color.RED)
            } else {
                binding.inputVariableKey.setTextColor(defaultColor)
            }
            binding.inputUrlEncode.isChecked = viewState.urlEncodeChecked
            binding.inputJsonEncode.isChecked = viewState.jsonEncodeChecked
            binding.inputAllowShare.isChecked = viewState.allowShareChecked
            if (viewState.shareSupportVisible) {
                binding.inputShareSupport.selectedItem = viewState.shareSupport.name
            }
            binding.inputShareSupport.isVisible = viewState.shareSupportVisible
            setDialogState(viewState.dialogState, viewModel)
        }
        collectEventsWhileActive(viewModel, ::handleEvent)
    }
     */

    class IntentBuilder(type: VariableType) : BaseIntentBuilder(VariableEditorActivity::class) {

        init {
            intent.putExtra(EXTRA_VARIABLE_TYPE, type.type)
        }

        fun variableId(variableId: VariableId) = also {
            intent.putExtra(EXTRA_VARIABLE_ID, variableId)
        }
    }

    companion object {
        private const val EXTRA_VARIABLE_ID = "ch.rmy.android.http_shortcuts.activities.variables.editor.VariableEditorActivity.variable_id"
        private const val EXTRA_VARIABLE_TYPE = "ch.rmy.android.http_shortcuts.activities.variables.editor.VariableEditorActivity.variable_type"
    }
}
