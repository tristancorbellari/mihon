package tachiyomi.data.tab

import kotlinx.coroutines.flow.Flow
import tachiyomi.data.Database
import tachiyomi.data.DatabaseHandler
import tachiyomi.domain.tab.model.Tab
import tachiyomi.domain.tab.model.TabUpdate
import tachiyomi.domain.tab.repository.TabRepository

class TabRepositoryImpl(
    private val handler: DatabaseHandler,
) : TabRepository {

    override suspend fun get(id: Long): Tab? {
        return handler.awaitOneOrNull { tabsQueries.getTab(id, ::mapTab) }
    }

    override suspend fun getAll(): List<Tab> {
        return handler.awaitList { tabsQueries.getTabs(::mapTab) }
    }

    override fun getAllAsFlow(): Flow<List<Tab>> {
        return handler.subscribeToList { tabsQueries.getTabs(::mapTab) }
    }

    override suspend fun updatePartial(update: TabUpdate) {
        handler.await {
            updatePartialBlocking(update)
        }
    }

    override suspend fun updatePartial(updates: List<TabUpdate>) {
        handler.await(inTransaction = true) {
            for (update in updates) {
                updatePartialBlocking(update)
            }
        }
    }

    private fun Database.updatePartialBlocking(update: TabUpdate) {
        tabsQueries.update(
            name = update.name,
            order = update.order,
            // flags = update.flags,
            enabled = update.enabled,
            tabId = update.id,
        )
    }

    private fun mapTab(
        id: Long,
        name: String,
        order: Long,
        enabled: Boolean,
    ): Tab {
        return Tab(
            id = id,
            name = name,
            order = order,
            enabled = enabled,
        )
    }
}
