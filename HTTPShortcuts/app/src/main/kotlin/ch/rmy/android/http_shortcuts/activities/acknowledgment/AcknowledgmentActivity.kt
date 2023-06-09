package ch.rmy.android.http_shortcuts.activities.acknowledgment

import androidx.compose.runtime.Composable
import ch.rmy.android.framework.ui.BaseIntentBuilder
import ch.rmy.android.http_shortcuts.activities.BaseComposeActivity

class AcknowledgmentActivity : BaseComposeActivity() {

    @Composable
    override fun Content() {
        AcknowledgmentScreen()
    }

    class IntentBuilder : BaseIntentBuilder(AcknowledgmentActivity::class)
}
