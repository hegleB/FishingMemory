package com.qure.domain.usecase.memo

import com.qure.domain.entity.memo.Memo
import com.qure.domain.entity.memo.MemoQuery
import com.qure.domain.repository.MemoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetFilteredMemoUseCase
    @Inject
    constructor(
        private val memoRepository: MemoRepository,
    ) {
        operator fun invoke(): Flow<List<Memo>> {
            return memoRepository.getfilteredMemo()
                .map { memos -> memos.sortedByDescending { memo -> memo.createTime } }
        }
    }
