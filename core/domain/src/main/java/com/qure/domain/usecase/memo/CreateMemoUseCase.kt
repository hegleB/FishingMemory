package com.qure.domain.usecase.memo

import com.qure.data.repository.memo.MemoRepository
import com.qure.model.memo.Memo
import com.qure.model.memo.MemoFields
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
