package eu.kanade.tachiyomi.ui.tab

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.util.fastMap
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import eu.kanade.presentation.tab.TabScreen
// import eu.kanade.presentation.tab.components.TabCreateDialog
// import eu.kanade.presentation.tab.components.TabDeleteDialog
// import eu.kanade.presentation.tab.components.TabRenameDialog
import eu.kanade.presentation.util.Screen
import eu.kanade.tachiyomi.util.system.toast
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.collectLatest
import tachiyomi.domain.tab.model.Tab
import tachiyomi.presentation.core.screens.LoadingScreen

class TabScreen : Screen() {

    @Composable
    override fun Content() {
        val context = LocalContext.current
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = rememberScreenModel { TabScreenModel() }

        val state by screenModel.state.collectAsState()

        if (state is TabScreenState.Loading) {
            LoadingScreen()
            return
        }

        val successState = state as TabScreenState.Success

        TabScreen(
            state = successState,
            // onClickCreate = { screenModel.showDialog(TabDialog.Create) },
            // onClickRename = { screenModel.showDialog(TabDialog.Rename(it)) },
            // onClickDelete = { screenModel.showDialog(TabDialog.Delete(it)) },
            // onClickEnable = { screenModel.showDialog(TabDialog.Enable(it)) },
            // onClickDisable = { screenModel.showDialog(TabDialog.Disable(it)) },
            onClickEnable = screenModel::enableTab,
            onClickDisable = screenModel::disableTab,
            onChangeOrder = screenModel::changeOrder,
            navigateUp = navigator::pop,
        )

//         when (val dialog = successState.dialog) {
//             null -> {}
// //             TabDialog.Create -> {
// //                 TabCreateDialog(
// //                     onDismissRequest = screenModel::dismissDialog,
// // //                    onCreate = screenModel::createTab,
// //                     onCreate = {},
// //                     tabs = successState.tabs.fastMap { it.name }.toImmutableList(),
// //                 )
// //             }
//             is TabDialog.Enable -> {
//                 TabEnableDialog(
//                     onDismissRequest = screenModel::dismissDialog,
//                     onEnable = { screenModel.enableTab(dialog.tab, it) },
//                     // onEnable = { },
//                     tabs = successState.tabs.fastMap { it.name }.toImmutableList(),
//                     tab = dialog.tab.name,
//                 )
//             }
//             is TabDialog.Disable -> {
//                 TabDisableDialog(
//                     onDismissRequest = screenModel::dismissDialog,
//                     onDisable = { screenModel.disableTab(dialog.tab.id) },
//                     // Disable = { },
//                     tab = dialog.tab.name,
//                 )
//             }
//         }

        LaunchedEffect(Unit) {
            screenModel.events.collectLatest { event ->
                if (event is TabEvent.LocalizedMessage) {
                    context.toast(event.stringRes)
                }
            }
        }
    }
}
