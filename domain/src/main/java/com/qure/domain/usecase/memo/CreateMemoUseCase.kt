package com.qure.domain.usecase.memo

import com.qure.domain.entity.memo.Memo
import com.qure.domain.entity.memo.MemoFields
import com.qure.domain.repository.MemoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreateMemoUseCase
    @Inject
    constructor(
        private val memoRepository: MemoRepository,
    ) {
        operator fun invoke(memoFields: MemoFields): Flow<Memo> {
            return flow {
                emit(memoRepository.createMemo(memoFields))
            }
        }
    }
