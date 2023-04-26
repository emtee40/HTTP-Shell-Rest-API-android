package ch.rmy.android.http_shortcuts.extensions

import android.content.Context
import ch.rmy.android.framework.extensions.fromHexString
import ch.rmy.android.http_shortcuts.R
import ch.rmy.android.http_shortcuts.data.dtos.ShortcutPlaceholder
import ch.rmy.android.http_shortcuts.data.enums.ShortcutExecutionType
import ch.rmy.android.http_shortcuts.data.models.Shortcut
import ch.rmy.android.http_shortcuts.http.CertificatePin
import ch.rmy.android.http_shortcuts.data.models.CertificatePin as CertificatePinModel

val Shortcut.type: ShortcutExecutionType
    get() = ShortcutExecutionType.get(executionType!!)

fun Shortcut.toShortcutPlaceholder() =
    ShortcutPlaceholder(
        id = id,
        name = name,
        icon = icon,
    )

fun Shortcut.getSafeName(context: Context) =
    name.ifEmpty { context.getString(R.string.shortcut_safe_name) }

val Shortcut.isTemporaryShortcut
    get() = id == Shortcut.TEMPORARY_ID

fun Shortcut.shouldIncludeInHistory() =
    !excludeFromHistory && !isTemporaryShortcut

fun CertificatePinModel.toCertificatePin(): CertificatePin =
    CertificatePin(
        pattern = pattern,
        hash = hash.fromHexString(),
    )
