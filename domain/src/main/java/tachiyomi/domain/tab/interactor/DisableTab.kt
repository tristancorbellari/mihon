package tachiyomi.domain.tab.interactor

import logcat.LogPriority
// import eu.kanade.domain.ui.UiPreferences
import tachiyomi.core.common.util.lang.withNonCancellableContext
import tachiyomi.core.common.util.system.logcat
import tachiyomi.domain.tab.model.TabUpdate
import tachiyomi.domain.tab.repository.TabRepository
// import tachiyomi.domain.download.service.DownloadPreferences
// import tachiyomi.domain.library.service.LibraryPreferences

class DisableTab(
    private val tabRepository: TabRepository,
    // private val uiPreferences: UiPreferences,
    // private val libraryPreferences: LibraryPreferences,
    // private val downloadPreferences: DownloadPreferences,
) {

    suspend fun await(tabId: Long) = withNonCancellableContext {
        try {
            tabRepository.disableTab(tabId)
        } catch (e: Exception) {
            logcat(LogPriority.ERROR, e)
            return@withNonCancellableContext Result.InternalError(e)
        }

        val tabs = tabRepository.getAll()
        val updates = tabs.mapIndexed { index, tab ->
            TabUpdate(
                id = tab.id,
                order = index.toLong(),
            )
        }

        // val defaultTab = uiPreferences.defaultTab().get()
        // if (defaultTab == tabId.toInt()) {
        //     uiPreferences.defaultTab().delete()
        // }

        // val tabPreferences = listOf(
        //     uiPreferences.updateTabs(),
        //     uiPreferences.updateTabsExclude(),
        //     // downloadPreferences.removeExcludeTabs(),
        //     // downloadPreferences.downloadNewChapterTabs(),
        //     // downloadPreferences.downloadNewChapterTabsExclude(),
        // )
        // val tabIdString = tabId.toString()
        // tabPreferences.forEach { preference ->
        //     val ids = preference.get()
        //     if (tabIdString !in ids) return@forEach
        //     preference.set(ids.minus(tabIdString))
        // }

        try {
            tabRepository.updatePartial(updates)
            Result.Success
        } catch (e: Exception) {
            logcat(LogPriority.ERROR, e)
            Result.InternalError(e)
        }
    }

    sealed interface Result {
        data object Success : Result
        data class InternalError(val error: Throwable) : Result
    }
}
