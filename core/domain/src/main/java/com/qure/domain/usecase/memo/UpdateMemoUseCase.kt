package com.qure.domain.usecase.memo

import com.qure.data.repository.memo.MemoRepository
import com.qure.model.memo.Document
import com.qure.model.memo.MemoFields
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateMemoUseCase
    @Inject
    constructor(
        private val memoRepository: MemoRepository,
    ) {
        operator fun invoke(memoFields: MemoFields): Flow<Document> {
            return memoRepository.getUpdatedMemo(memoFields)
        }
    }
