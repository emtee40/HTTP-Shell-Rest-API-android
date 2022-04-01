package ch.rmy.android.http_shortcuts.scripting.actions.types

import ch.rmy.android.http_shortcuts.scripting.ActionAlias
import ch.rmy.android.http_shortcuts.scripting.actions.ActionDTO

class DialogActionType : BaseActionType() {

    override val type = TYPE

    override fun fromDTO(actionDTO: ActionDTO) = DialogAction(
        message = actionDTO.getString(0) ?: "",
        title = actionDTO.getString(1) ?: "",
    )

    override fun getAlias() = ActionAlias(
        functionName = FUNCTION_NAME,
        functionNameAliases = setOf(FUNCTION_NAME_ALIAS),
        parameters = 2,
    )

    companion object {
        private const val TYPE = "show_dialog"
        private const val FUNCTION_NAME = "showDialog"
        private const val FUNCTION_NAME_ALIAS = "alert"
    }
}
