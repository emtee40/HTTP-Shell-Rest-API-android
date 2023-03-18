package ch.rmy.android.http_shortcuts.data.domains.history

import ch.rmy.android.framework.data.BaseRepository
import ch.rmy.android.framework.data.RealmFactory
import ch.rmy.android.framework.utils.UUIDUtils.newUUID
import ch.rmy.android.http_shortcuts.data.domains.getHistoryEvents
import ch.rmy.android.http_shortcuts.data.enums.HistoryEventType
import ch.rmy.android.http_shortcuts.data.models.HistoryEvent
import ch.rmy.android.http_shortcuts.data.models.HistoryEvent.Companion.FIELD_TIME
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import javax.inject.Inject
import kotlin.time.Duration
import ch.rmy.android.framework.extensions.minus

class HistoryRepository
@Inject
constructor(
    realmFactory: RealmFactory,
) : BaseRepository(realmFactory) {

    fun getObservableHistory(maxAge: Duration): Flow<List<HistoryEvent>> =
        observeQuery {
            getHistoryEvents()
                .greaterThan(FIELD_TIME, Instant.now() - maxAge)
        }

    suspend fun deleteHistory() {
        commitTransaction {
            getHistoryEvents()
                .findAll()
                .deleteAllFromRealm()
        }
    }

    suspend fun deleteOldEvents(maxAge: Duration) {
        commitTransaction {
            getHistoryEvents()
                .lessThan(FIELD_TIME, Date().apply { time -= maxAge.inWholeMilliseconds })
                .findAll()
                .deleteAllFromRealm()
        }
    }

    suspend fun storeHistoryEvent(type: HistoryEventType, data: Any?) {
        commitTransaction {
            copy(
                HistoryEvent(id = newUUID(), eventType = type, eventData = data)
            )
        }
    }
}
