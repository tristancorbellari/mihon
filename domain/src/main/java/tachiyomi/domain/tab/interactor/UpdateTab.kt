package tachiyomi.domain.tab.interactor

import tachiyomi.core.common.util.lang.withNonCancellableContext
import tachiyomi.domain.tab.model.TabUpdate
import tachiyomi.domain.tab.repository.TabRepository

class UpdateTab(
    private val tabRepository: TabRepository,
) {

    suspend fun await(payload: TabUpdate): Result = withNonCancellableContext {
        try {
            tabRepository.updatePartial(payload)
            Result.Success
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    sealed interface Result {
        data object Success : Result
        data class Error(val error: Exception) : Result
    }
}
