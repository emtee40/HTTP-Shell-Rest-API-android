package ch.rmy.android.http_shortcuts.activities.moving

import androidx.compose.runtime.Composable
import ch.rmy.android.framework.ui.BaseIntentBuilder
import ch.rmy.android.http_shortcuts.activities.BaseComposeActivity

class MoveActivity : BaseComposeActivity() {

    @Composable
    override fun Content() {
        MoveScreen()
    }

    class IntentBuilder : BaseIntentBuilder(MoveActivity::class)
}
