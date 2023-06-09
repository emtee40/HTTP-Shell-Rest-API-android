package ch.rmy.android.http_shortcuts.scripting.actions.types

import android.content.Context
import ch.rmy.android.framework.extensions.takeUnlessEmpty
import ch.rmy.android.http_shortcuts.R
import ch.rmy.android.http_shortcuts.activities.execute.ExecuteDialogState
import ch.rmy.android.http_shortcuts.dagger.ApplicationComponent
import ch.rmy.android.http_shortcuts.exceptions.DialogCancellationException
import ch.rmy.android.http_shortcuts.exceptions.UserException
import ch.rmy.android.http_shortcuts.scripting.ExecutionContext
import ch.rmy.android.http_shortcuts.utils.ActivityProvider
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale
import javax.inject.Inject

class PromptTimeAction(
    private val format: String?,
    private val initialTime: String?,
) : BaseAction() {

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var activityProvider: ActivityProvider

    override fun inject(applicationComponent: ApplicationComponent) {
        applicationComponent.inject(this)
    }

    override suspend fun execute(executionContext: ExecutionContext): String? =
        try {
            val selectedTime = executionContext.dialogHandle.showDialog(
                ExecuteDialogState.TimePicker(
                    initialTime = getInitialTime(),
                )
            )
            val pattern = format ?: DEFAULT_FORMAT
            try {
                DateTimeFormatter.ofPattern(pattern, Locale.US)
                    .format(selectedTime)
            } catch (e: IllegalArgumentException) {
                throw UserException.create {
                    getString(R.string.error_invalid_time_format)
                }
            }
        } catch (e: DialogCancellationException) {
            null
        }

    private fun getInitialTime(): LocalTime =
        initialTime
            ?.takeUnlessEmpty()
            ?.let { timeString ->
                try {
                    LocalTime.parse(timeString, DateTimeFormatter.ofPattern(DEFAULT_FORMAT, Locale.US))
                } catch (e: DateTimeParseException) {
                    null
                }
            }
            ?: LocalTime.now()

    companion object {
        private const val DEFAULT_FORMAT = "HH:mm"
    }
}
