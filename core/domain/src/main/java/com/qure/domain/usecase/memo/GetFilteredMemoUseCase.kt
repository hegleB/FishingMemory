package com.qure.domain.usecase.memo

import com.qure.data.repository.memo.MemoRepository
import com.qure.model.memo.Memo
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
            .map { memos ->
                memos.filter { it.createTime != "" }
                    .sortedByDescending { memo -> memo.createTime }
            }
    }
}
