package com.qure.domain.usecase.memo

import com.qure.domain.entity.memo.Memo
import com.qure.domain.entity.memo.MemoQuery
import com.qure.domain.repository.MemoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFilteredMemoUseCase @Inject constructor(
    private val memoRepository: MemoRepository,
) {
    operator fun invoke(memoQuery: MemoQuery): Flow<Result<List<Memo>>> {
        return memoRepository.getfilteredMemo(memoQuery)
    }
}