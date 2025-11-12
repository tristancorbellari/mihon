package eu.kanade.presentation.tab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import eu.kanade.presentation.tab.components.EnabledTabListItem
import eu.kanade.presentation.tab.components.DisabledTabListItem
import eu.kanade.presentation.tab.components.TabFloatingActionButton
import eu.kanade.presentation.components.AppBar
import eu.kanade.tachiyomi.ui.tab.TabScreenState
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import tachiyomi.domain.tab.model.Tab
import tachiyomi.i18n.MR
import tachiyomi.presentation.core.components.material.Scaffold
import tachiyomi.presentation.core.components.material.padding
import tachiyomi.presentation.core.components.material.topSmallPaddingValues
import tachiyomi.presentation.core.i18n.stringResource
import tachiyomi.presentation.core.util.plus

@Composable
fun TabScreen(
    state: TabScreenState.Success,
    // onClickCreate: () -> Unit,
    // onClickRename: (Tab) -> Unit,
    // onClickDelete: (Tab) -> Unit,
    onClickEnable: (Long) -> Unit,
    onClickDisable: (Long) -> Unit,
    onChangeOrder: (Tab, Int) -> Unit,
    navigateUp: () -> Unit,
) {
    val lazyListState = rememberLazyListState()
    Scaffold(
        topBar = { scrollBehavior ->
            AppBar(
                title = stringResource(MR.strings.action_edit_tabs),
                navigateUp = navigateUp,
                scrollBehavior = scrollBehavior,
            )
        },
        // floatingActionButton = {
        //     TabFloatingActionButton(
        //         lazyListState = lazyListState,
        //         onCreate = onClickCreate,
        //     )
        // },
    ) { paddingValues ->
        TabContent(
            tabs = state.tabs,
            lazyListState = lazyListState,
            paddingValues = paddingValues,
            // onClickRename = onClickRename,
            // onClickDelete = onClickDelete,
            onClickEnable = onClickEnable,
            onClickDisable = onClickDisable,
            onChangeOrder = onChangeOrder,
        )
    }
}

@Composable
private fun TabContent(
    tabs: List<Tab>,
    lazyListState: LazyListState,
    paddingValues: PaddingValues,
    // onClickRename: (Tab) -> Unit,
    // onClickDelete: (Tab) -> Unit,
    onClickEnable: (Long) -> Unit,
    onClickDisable: (Long) -> Unit,
    onChangeOrder: (Tab, Int) -> Unit,
) {
    val enabledTabsState = remember { tabs.filter { it.enabled }.toMutableStateList() }
    val disabledTabsState = remember { tabs.filter { !it.enabled }.toMutableStateList() }
    val reorderableState = rememberReorderableLazyListState(lazyListState, paddingValues) { from, to ->
        val fromIndex = from.index - 1
        val toIndex = to.index - 1

        val item = enabledTabsState.removeAt(fromIndex)
        enabledTabsState.add(toIndex, item)
        onChangeOrder(item, toIndex)
    }

    LaunchedEffect(tabs) {
        if (!reorderableState.isAnyItemDragging) {
            enabledTabsState.clear()
            enabledTabsState.addAll(tabs.filter { it.enabled })

            disabledTabsState.clear()
            disabledTabsState.addAll(tabs.filter { !it.enabled })
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = lazyListState,
        contentPadding = paddingValues +
            topSmallPaddingValues +
            PaddingValues(horizontal = MaterialTheme.padding.medium),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.padding.small),
    ) {
        item {
            Text(
                // text = stringResource(MR.strings.pinned_sources)
                text = "Enabled",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = MaterialTheme.padding.small)
            )
        }

        items(
            items = enabledTabsState,
            key = { tab -> tab.key },
        ) { tab ->
            ReorderableItem(reorderableState, tab.key) {
                EnabledTabListItem(
                    modifier = Modifier.animateItem(),
                    tab = tab,
                    // onRename = { onClickRename(tab) },
                    // onDelete = { onClickDelete(tab) },
                    // onEnable = { onClickEnable(tab) },
                    onDisable = { onClickDisable(tab.id) },
                )
            }
        }

        item {
            Text(
                // text = stringResource(MR.strings.pinned_sources)
                text = "Disabled",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = MaterialTheme.padding.small)
            )
        }

        items(
            items = disabledTabsState,
            key = { tab -> tab.key },
        ) { tab ->
                DisabledTabListItem(
                    modifier = Modifier.animateItem(),
                    tab = tab,
                    // onRename = { onClickRename(tab) },
                    // onDelete = { onClickDelete(tab) },
                    onEnable = { onClickEnable(tab.id) },
                    // onDisable = { onClickDisable(tab) },
                )
        }
    }
}

private val Tab.key inline get() = "tab-$id"
