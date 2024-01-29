package com.qure.domain.usecase.memo

import com.qure.domain.entity.memo.Memo
import com.qure.domain.entity.memo.MemoFields
import com.qure.domain.repository.MemoRepository
import javax.inject.Inject

class CreateMemoUseCase
    @Inject
    constructor(
        private val memoRepository: MemoRepository,
    ) {
        suspend operator fun invoke(memoFields: MemoFields): Result<Memo> {
            return memoRepository.createMemo(memoFields)
                .onSuccess {
                }.onFailure {
                    throw it as Exception
                }
        }
    }
