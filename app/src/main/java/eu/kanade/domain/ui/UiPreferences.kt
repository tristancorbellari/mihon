package eu.kanade.domain.ui

import eu.kanade.domain.ui.model.AppTheme
import eu.kanade.domain.ui.model.TabletUiMode
import eu.kanade.domain.ui.model.ThemeMode
import eu.kanade.tachiyomi.util.system.DeviceUtil
import eu.kanade.tachiyomi.util.system.isDynamicColorAvailable
import tachiyomi.core.common.preference.Preference
import tachiyomi.core.common.preference.PreferenceStore
import tachiyomi.core.common.preference.getEnum
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

class UiPreferences(
    private val preferenceStore: PreferenceStore,
) {

    fun themeMode() = preferenceStore.getEnum("pref_theme_mode_key", ThemeMode.SYSTEM)

    fun appTheme() = preferenceStore.getEnum(
        "pref_app_theme",
        if (DeviceUtil.isDynamicColorAvailable) {
            AppTheme.MONET
        } else {
            AppTheme.DEFAULT
        },
    )

    fun themeDarkAmoled() = preferenceStore.getBoolean("pref_theme_dark_amoled_key", false)

    fun relativeTime() = preferenceStore.getBoolean("relative_time_v2", true)

    fun dateFormat() = preferenceStore.getString("app_date_format", "")

    fun tabletUiMode() = preferenceStore.getEnum("tablet_ui_mode", TabletUiMode.AUTOMATIC)

    fun imagesInDescription() = preferenceStore.getBoolean("pref_render_images_description", true)

    // region Tab

    fun defaultTab() = preferenceStore.getInt(DEFAULT_TAB_PREF_KEY, -1)

    fun lastUsedTab() = preferenceStore.getInt(Preference.appStateKey("last_used_tab"), 0)

    // fun categoryTabs() = preferenceStore.getBoolean("display_category_tabs", true)

    // fun categoryNumberOfItems() = preferenceStore.getBoolean("display_number_of_items", false)

    // fun categorizedDisplaySettings() = preferenceStore.getBoolean("categorized_display", false)

    fun updateTabs() = preferenceStore.getStringSet(LIBRARY_UPDATE_TABS_PREF_KEY, emptySet())

    fun updateTabsExclude() = preferenceStore.getStringSet(LIBRARY_UPDATE_TABS_EXCLUDE_PREF_KEY, emptySet())

    // endregion

    companion object {
        fun dateFormat(format: String): DateTimeFormatter = when (format) {
            "" -> DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
            else -> DateTimeFormatter.ofPattern(format, Locale.getDefault())
        }

        const val DEFAULT_TAB_PREF_KEY = "default_tab"
        private const val LIBRARY_UPDATE_TABS_PREF_KEY = "library_update_tabs"
        private const val LIBRARY_UPDATE_TABS_EXCLUDE_PREF_KEY = "library_update_tabs_exclude"
        val tabPreferenceKeys = setOf(
            DEFAULT_TAB_PREF_KEY,
            LIBRARY_UPDATE_TABS_PREF_KEY,
            LIBRARY_UPDATE_TABS_EXCLUDE_PREF_KEY,
        )
    }
}
