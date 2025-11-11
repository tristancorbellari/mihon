package tachiyomi.domain.tab.interactor

import kotlinx.coroutines.flow.Flow
import tachiyomi.domain.tab.model.Tab
import tachiyomi.domain.tab.repository.TabRepository

class GetTabs(
    private val tabRepository: TabRepository,
) {

    fun subscribe(): Flow<List<Tab>> {
        return tabRepository.getAllAsFlow()
    }

    suspend fun await(): List<Tab> {
        return tabRepository.getAll()
    }
}
