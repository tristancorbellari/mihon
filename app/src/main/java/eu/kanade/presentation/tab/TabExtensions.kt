package eu.kanade.presentation.tab

import android.content.Context
import androidx.compose.runtime.Composable
import tachiyomi.core.common.i18n.stringResource
import tachiyomi.domain.tab.model.Tab
import tachiyomi.i18n.MR
import tachiyomi.presentation.core.i18n.stringResource

val Tab.visualName: String
    @Composable
    get() = name

fun Tab.visualName(context: Context): String = name
