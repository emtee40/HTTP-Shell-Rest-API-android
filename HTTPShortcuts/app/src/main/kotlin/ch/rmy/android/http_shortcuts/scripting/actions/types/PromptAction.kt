package ch.rmy.android.http_shortcuts.scripting.actions.types

import ch.rmy.android.framework.extensions.toLocalizable
import ch.rmy.android.http_shortcuts.activities.execute.ExecuteDialogState
import ch.rmy.android.http_shortcuts.dagger.ApplicationComponent
import ch.rmy.android.http_shortcuts.exceptions.DialogCancellationException
import ch.rmy.android.http_shortcuts.scripting.ExecutionContext
import ch.rmy.android.http_shortcuts.utils.ActivityProvider
import ch.rmy.android.http_shortcuts.variables.Variables
import javax.inject.Inject

class PromptAction(private val message: String, private val prefill: String) : BaseAction() {

    @Inject
    lateinit var activityProvider: ActivityProvider

    override fun inject(applicationComponent: ApplicationComponent) {
        applicationComponent.inject(this)
    }

    override suspend fun execute(executionContext: ExecutionContext): String? {
        val finalMessage = Variables.rawPlaceholdersToResolvedValues(
            message,
            executionContext.variableManager.getVariableValuesByIds(),
        )

        if (finalMessage.isEmpty()) {
            return null
        }

        return try {
            executionContext.dialogHandle.showDialog(
                ExecuteDialogState.TextInput(
                    message = finalMessage.toLocalizable(),
                    type = ExecuteDialogState.TextInput.Type.TEXT,
                    initialValue = prefill,
                )
            )
        } catch (e: DialogCancellationException) {
            null
        }
    }
}
