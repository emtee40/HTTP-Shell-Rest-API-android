package ch.rmy.android.http_shortcuts.data.models

import ch.rmy.android.framework.utils.UUIDUtils.newUUID
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.Required

@RealmClass(name = "ResolvedVariable")
open class ResolvedVariableModel(
    @Required
    var key: String = "",
    @Required
    var value: String = "",
) : RealmObject() {
    @PrimaryKey
    @Required
    var id: String = newUUID()
}