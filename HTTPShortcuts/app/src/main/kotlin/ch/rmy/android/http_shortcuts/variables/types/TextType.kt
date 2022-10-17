package ch.rmy.android.http_shortcuts.variables.types

import android.content.Context
import android.text.InputType
import ch.rmy.android.http_shortcuts.dagger.ApplicationComponent
import ch.rmy.android.http_shortcuts.data.domains.variables.VariableRepository
import ch.rmy.android.http_shortcuts.data.models.VariableModel
import ch.rmy.android.http_shortcuts.extensions.canceledByUser
import ch.rmy.android.http_shortcuts.extensions.showOrElse
import ch.rmy.android.http_shortcuts.utils.ActivityProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume

class TextType : BaseVariableType() {

    @Inject
    lateinit var variablesRepository: VariableRepository

    @Inject
    lateinit var activityProvider: ActivityProvider

    override fun inject(applicationComponent: ApplicationComponent) {
        applicationComponent.inject(this)
    }

    override suspend fun resolveValue(context: Context, variable: VariableModel): String {
        val value = withContext(Dispatchers.Main) {
            suspendCancellableCoroutine<String> { continuation ->
                createDialogBuilder(activityProvider.getActivity(), variable, continuation)
                    .textInput(
                        prefill = variable.value?.takeIf { variable.rememberValue } ?: "",
                        inputType = InputType.TYPE_CLASS_TEXT or (if (variable.isMultiline) InputType.TYPE_TEXT_FLAG_MULTI_LINE else 0),
                        callback = continuation::resume,
                    )
                    .showOrElse {
                        continuation.canceledByUser()
                    }
            }
        }
        if (variable.rememberValue) {
            variablesRepository.setVariableValue(variable.id, value)
        }
        return value
    }
}
