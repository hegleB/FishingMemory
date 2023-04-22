package com.qure.domain.usecase.memo

import com.qure.domain.entity.memo.Document
import com.qure.domain.entity.memo.MemoFields
import com.qure.domain.repository.MemoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateMemoUseCase @Inject constructor(
    private val memoRepository: MemoRepository,
) {
    operator fun invoke(memoFields: MemoFields): Flow<Result<Document>> {
        return memoRepository.getUpdatedMemo(memoFields)
    }
}