package tachiyomi.domain.tab.interactor

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import logcat.LogPriority
import tachiyomi.core.common.util.lang.withNonCancellableContext
import tachiyomi.core.common.util.system.logcat
import tachiyomi.domain.tab.model.Tab
import tachiyomi.domain.tab.model.TabUpdate
import tachiyomi.domain.tab.repository.TabRepository

class ReorderTab(
    private val tabRepository: TabRepository,
) {
    private val mutex = Mutex()

    suspend fun await(tab: Tab, newIndex: Int) = withNonCancellableContext {
        mutex.withLock {
            val tabs = tabRepository.getAll()
                // .filterNot(Tab::isSystemTab)
                .toMutableList()

            val currentIndex = tabs.indexOfFirst { it.id == tab.id }
            if (currentIndex == -1) {
                return@withNonCancellableContext Result.Unchanged
            }

            try {
                tabs.add(newIndex, tabs.removeAt(currentIndex))

                val updates = tabs.mapIndexed { index, tab ->
                    TabUpdate(
                        id = tab.id,
                        order = index.toLong(),
                    )
                }

                tabRepository.updatePartial(updates)
                Result.Success
            } catch (e: Exception) {
                logcat(LogPriority.ERROR, e)
                Result.InternalError(e)
            }
        }
    }

    sealed interface Result {
        data object Success : Result
        data object Unchanged : Result
        data class InternalError(val error: Throwable) : Result
    }
}
