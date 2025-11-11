package tachiyomi.domain.tab.model

data class TabUpdate(
    val id: Long,
    val name: String? = null,
    val order: Long? = null,
    val enabled: Boolean? = null,
)
