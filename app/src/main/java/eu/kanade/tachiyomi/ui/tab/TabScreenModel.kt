package eu.kanade.tachiyomi.ui.tab

import androidx.compose.runtime.Immutable
import eu.kanade.domain.ui.UiPreferences
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.icerock.moko.resources.StringResource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
//import tachiyomi.domain.tab.interactor.CreateTabWithName
//import tachiyomi.domain.tab.interactor.DeleteTab
import tachiyomi.domain.tab.interactor.EnableTab
import tachiyomi.domain.tab.interactor.DisableTab
import tachiyomi.domain.tab.interactor.GetTabs
//import tachiyomi.domain.tab.interactor.RenameTab
import tachiyomi.domain.tab.interactor.ReorderTab
import tachiyomi.domain.tab.model.Tab
import tachiyomi.i18n.MR
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class TabScreenModel(
    private val uiPreferences: UiPreferences = Injekt.get(),
    private val getTabs: GetTabs = Injekt.get(),
//    private val createTabWithName: CreateTabWithName = Injekt.get(),
//    private val deleteTab: DeleteTab = Injekt.get(),
    private val enableTab: EnableTab = Injekt.get(),
    private val disableTab: DisableTab = Injekt.get(),
    private val reorderTab: ReorderTab = Injekt.get(),
//    private val renameTab: RenameTab = Injekt.get(),
) : StateScreenModel<TabScreenState>(TabScreenState.Loading) {

    private val _events: Channel<TabEvent> = Channel()
    val events = _events.receiveAsFlow()

    init {
        screenModelScope.launch {
            getTabs.subscribe()
                .collectLatest { tabs ->
                    mutableState.update {
                        TabScreenState.Success(
                            tabs = tabs
                                // .filterNot(Tab::isSystemTab)
                                .toImmutableList(),
                        )
                    }
                }
        }
    }

//    fun createTab(name: String) {
//        screenModelScope.launch {
//            when (createTabWithName.await(name)) {
//                is CreateTabWithName.Result.InternalError -> _events.send(TabEvent.InternalError)
//                else -> {}
//            }
//        }
//    }
//
//    fun deleteTab(tabId: Long) {
//        screenModelScope.launch {
//            when (deleteTab.await(tabId = tabId)) {
//                is DeleteTab.Result.InternalError -> _events.send(TabEvent.InternalError)
//                else -> {}
//            }
//        }
//    }
//

    fun enableTab(tabId: Long) {
        screenModelScope.launch {
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

            when (enableTab.await(tabId = tabId)) {
                is EnableTab.Result.InternalError -> _events.send(TabEvent.InternalError)
                else -> {}
            }
        }
    }

    fun disableTab(tabId: Long) {
        screenModelScope.launch {
            val defaultTab = uiPreferences.defaultTab().get()
            if (defaultTab == tabId.toInt()) {
                uiPreferences.defaultTab().delete()
            }

            val tabPreferences = listOf(
                uiPreferences.updateTabs(),
                uiPreferences.updateTabsExclude(),
                // downloadPreferences.removeExcludeTabs(),
                // downloadPreferences.downloadNewChapterTabs(),
                // downloadPreferences.downloadNewChapterTabsExclude(),
            )
            val tabIdString = tabId.toString()
            tabPreferences.forEach { preference ->
                val ids = preference.get()
                if (tabIdString !in ids) return@forEach
                preference.set(ids.minus(tabIdString))
            }

            when (disableTab.await(tabId = tabId)) {
                is DisableTab.Result.InternalError -> _events.send(TabEvent.InternalError)
                else -> {}
            }
        }
    }

    fun changeOrder(tab: Tab, newIndex: Int) {
        screenModelScope.launch {
            when (reorderTab.await(tab, newIndex)) {
                is ReorderTab.Result.InternalError -> _events.send(TabEvent.InternalError)
                else -> {}
            }
        }
    }

//    fun renameTab(tab: Tab, name: String) {
//        screenModelScope.launch {
//            when (renameTab.await(tab, name)) {
//                is RenameTab.Result.InternalError -> _events.send(TabEvent.InternalError)
//                else -> {}
//            }
//        }
//    }

    // fun showDialog(dialog: TabDialog) {
    //     mutableState.update {
    //         when (it) {
    //             TabScreenState.Loading -> it
    //             is TabScreenState.Success -> it.copy(dialog = dialog)
    //         }
    //     }
    // }

    // fun dismissDialog() {
    //     mutableState.update {
    //         when (it) {
    //             TabScreenState.Loading -> it
    //             is TabScreenState.Success -> it.copy(dialog = null)
    //         }
    //     }
    // }
}

// sealed interface TabDialog {
//     data object Create : TabDialog
//     data class Rename(val tab: Tab) : TabDialog
//     data class Delete(val tab: Tab) : TabDialog
// }

sealed interface TabEvent {
    sealed class LocalizedMessage(val stringRes: StringResource) : TabEvent
    data object InternalError : LocalizedMessage(MR.strings.internal_error)
}

sealed interface TabScreenState {

    @Immutable
    data object Loading : TabScreenState

    @Immutable
    data class Success(
        val tabs: ImmutableList<Tab>,
        // val dialog: TabDialog? = null,
    ) : TabScreenState {

        val isEmpty: Boolean
            get() = tabs.isEmpty()
    }
}
