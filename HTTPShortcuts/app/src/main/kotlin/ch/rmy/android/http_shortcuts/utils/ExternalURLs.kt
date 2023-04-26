package ch.rmy.android.http_shortcuts.utils

object ExternalURLs {

    const val SHORTCUTS_DOCUMENTATION = "https://http-shortcuts.rmy.ch/shortcuts"
    const val CATEGORIES_DOCUMENTATION = "https://http-shortcuts.rmy.ch/categories"
    const val VARIABLES_DOCUMENTATION = "https://http-shortcuts.rmy.ch/variables"
    const val SCRIPTING_DOCUMENTATION = "https://http-shortcuts.rmy.ch/scripting#scripting"
    const val IMPORT_EXPORT_DOCUMENTATION = "https://http-shortcuts.rmy.ch/import-export"
    const val CERTIFICATE_PINNING_DOCUMENTATION = "https://http-shortcuts.rmy.ch/advanced#certificate-pinning"

    const val PRIVACY_POLICY = "https://http-shortcuts.rmy.ch/privacy-policy"
    const val DOCUMENTATION_PAGE = "https://http-shortcuts.rmy.ch/documentation"
    const val DONATION_PAGE = "https://http-shortcuts.rmy.ch/support-me#donate"
    const val PLAY_STORE = "https://play.google.com/store/apps/details?id=ch.rmy.android.http_shortcuts"
    const val F_DROID = "https://f-droid.org/en/packages/ch.rmy.android.http_shortcuts/"
    const val GITHUB = "https://github.com/Waboodoo/HTTP-Shortcuts"
    const val TRANSLATION = "https://poeditor.com/join/project/8tHhwOTzVZ"

    fun getScriptingDocumentation(docRef: String) =
        "https://http-shortcuts.rmy.ch/scripting#$docRef"
}
