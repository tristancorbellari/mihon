package tachiyomi.domain.tab.repository

import kotlinx.coroutines.flow.Flow
import tachiyomi.domain.tab.model.Tab
import tachiyomi.domain.tab.model.TabUpdate

interface TabRepository {

    suspend fun get(id: Long): Tab?

    suspend fun getAll(): List<Tab>

    fun getAllAsFlow(): Flow<List<Tab>>

    // suspend fun getCategoriesByMangaId(mangaId: Long): List<Category>

    // fun getCategoriesByMangaIdAsFlow(mangaId: Long): Flow<List<Category>>

    // suspend fun insert(category: Category)

    suspend fun updatePartial(update: TabUpdate)

    suspend fun updatePartial(updates: List<TabUpdate>)

    // suspend fun updateAllFlags(flags: Long?)

    suspend fun enableTab(tabId: Long)

    suspend fun disableTab(tabId: Long)
}
