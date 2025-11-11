package tachiyomi.domain.tab.model

import java.io.Serializable

data class Tab(
    val id: Long,
    val name: String,
    val order: Long,
    val enabled: Boolean,
) : Serializable {

    val IsAlwaysEnabled: Boolean = id == ALWAYS_ENABLED_ID

    companion object {
        const val ALWAYS_ENABLED_ID = 4L
    }
}
