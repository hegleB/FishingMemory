package com.qure.domain.usecase.memo

import com.qure.domain.repository.MemoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteMemoUseCase
    @Inject
    constructor(
        private val memoRepository: MemoRepository,
    ) {
        suspend operator fun invoke(uuid: String): Flow<Unit> {
            return memoRepository.deleteMemo(uuid)
        }
    }
