package ch.rmy.android.http_shortcuts.data

import android.content.Context
import ch.rmy.android.framework.data.RealmContext
import ch.rmy.android.framework.data.RealmFactory
import ch.rmy.android.framework.data.RealmTransactionContext
import ch.rmy.android.framework.extensions.logException
import ch.rmy.android.framework.utils.UUIDUtils.newUUID
import ch.rmy.android.http_shortcuts.R
import ch.rmy.android.http_shortcuts.data.migration.DatabaseMigration
import ch.rmy.android.http_shortcuts.data.models.AppLock
import ch.rmy.android.http_shortcuts.data.models.Base
import ch.rmy.android.http_shortcuts.data.models.Category
import ch.rmy.android.http_shortcuts.data.models.CertificatePin
import ch.rmy.android.http_shortcuts.data.models.Header
import ch.rmy.android.http_shortcuts.data.models.HistoryEvent
import ch.rmy.android.http_shortcuts.data.models.Option
import ch.rmy.android.http_shortcuts.data.models.Parameter
import ch.rmy.android.http_shortcuts.data.models.PendingExecution
import ch.rmy.android.http_shortcuts.data.models.Repetition
import ch.rmy.android.http_shortcuts.data.models.ResolvedVariable
import ch.rmy.android.http_shortcuts.data.models.ResponseHandling
import ch.rmy.android.http_shortcuts.data.models.Shortcut
import ch.rmy.android.http_shortcuts.data.models.Variable
import ch.rmy.android.http_shortcuts.data.models.Widget
import io.realm.kotlin.MutableRealm
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import java.io.File

class RealmFactoryImpl private constructor() : RealmFactory {

    override fun getRealmContext(): RealmContext =
        RealmContext(realmInstance)

    override suspend fun updateRealm(transaction: RealmTransactionContext.() -> Unit) {
        realmInstance.write {
            RealmTransactionContext(this).transaction()
        }
    }

    companion object {

        private const val DB_NAME = "shortcuts_db_v2"
        private var instance: RealmFactory? = null
        private lateinit var realmInstance: Realm

        var isRealmAvailable: Boolean = true
            private set

        fun init(context: Context) {
            if (instance != null) {
                return
            }

            try {
                val configuration = createConfiguration(context)
                val backupFile = File("${configuration.path}.backup-copy")
                if (!backupFile.exists()) {
                    File(configuration.path).takeIf { it.exists() }?.copyTo(backupFile)
                }
                instance = RealmFactoryImpl()
                realmInstance = Realm.open(configuration)
            } catch (e: Exception) {
                logException(e)
                isRealmAvailable = false
            }
        }

        fun getInstance(): RealmFactory = instance!!

        private fun createConfiguration(context: Context): RealmConfiguration =
            RealmConfiguration.Builder(
                setOf(
                    AppLock::class,
                    Base::class,
                    Category::class,
                    CertificatePin::class,
                    Header::class,
                    HistoryEvent::class,
                    Option::class,
                    Parameter::class,
                    PendingExecution::class,
                    Repetition::class,
                    ResolvedVariable::class,
                    ResponseHandling::class,
                    Shortcut::class,
                    Variable::class,
                    Widget::class,
                )
            )
                .schemaVersion(DatabaseMigration.VERSION)
                .migration(DatabaseMigration())
                .initialData {
                    setupBase(context)
                }
                .name(DB_NAME)
                .compactOnLaunch()
                .build()

        private fun MutableRealm.setupBase(context: Context) {
            val defaultCategoryName = context.getString(R.string.shortcuts)
            val defaultCategory = Category(defaultCategoryName)
            defaultCategory.id = newUUID()

            val newBase = Base().apply {
                categories.add(defaultCategory)
                version = DatabaseMigration.VERSION
                compatibilityVersion = DatabaseMigration.COMPATIBILITY_VERSION
            }
            copyToRealm(newBase)
        }
    }
}
