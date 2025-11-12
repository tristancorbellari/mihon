package eu.kanade.presentation.tab.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DragHandle
import androidx.compose.material.icons.outlined.ToggleOff
import androidx.compose.material.icons.outlined.ToggleOn
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import sh.calvin.reorderable.ReorderableCollectionItemScope
import tachiyomi.domain.tab.model.Tab
import tachiyomi.i18n.MR
import tachiyomi.presentation.core.components.material.padding
import tachiyomi.presentation.core.i18n.stringResource

@Composable
fun ReorderableCollectionItemScope.EnabledTabListItem(
    tab: Tab,
    // onRename: () -> Unit,
    // onDelete: () -> Unit,
    onDisable: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                // .clickable(onClick = onRename)
                .clickable(onClick = onDisable)
                .padding(vertical = MaterialTheme.padding.small)
                .padding(
                    start = MaterialTheme.padding.small,
                    end = MaterialTheme.padding.medium,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Outlined.DragHandle,
                contentDescription = null,
                modifier = Modifier
                    .padding(MaterialTheme.padding.medium)
                    .draggableHandle(),
            )
            Text(
                text = "${tab.name} ${tab.order}",
                modifier = Modifier.weight(1f),
            )
            // IconButton(onClick = onRename) {
            IconButton(onClick = onDisable) {
                Icon(
                    imageVector = Icons.Outlined.ToggleOn,
                    contentDescription = stringResource(MR.strings.action_rename_category),
                )
            }
        }
    }
}

@Composable
fun DisabledTabListItem(
    tab: Tab,
    // onRename: () -> Unit,
    // onDelete: () -> Unit,
    onEnable: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                // .clickable(onClick = onRename)
                .clickable(onClick = onEnable)
                .padding(vertical = MaterialTheme.padding.small)
                .padding(
                    start = MaterialTheme.padding.small,
                    end = MaterialTheme.padding.medium,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "${tab.name} ${tab.order}",
                modifier = Modifier.weight(1f),
            )
            // IconButton(onClick = onDelete) {
            IconButton(onClick = onEnable) {
                Icon(
                    imageVector = Icons.Outlined.ToggleOff,
                    contentDescription = stringResource(MR.strings.action_rename_category),
                )
            }
        }
    }
}
