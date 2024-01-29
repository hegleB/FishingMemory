package com.qure.domain.usecase.member

import com.qure.domain.entity.memo.MemoQuery
import com.qure.domain.repository.AuthRepository
import com.qure.domain.repository.MemoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WithdrawServiceUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository,
        private val memoRepository: MemoRepository,
    ) {
        operator fun invoke(memoQuery: MemoQuery): Flow<Result<Boolean>> {
            authRepository.removeTokenFromLocal()
            authRepository.removeTokenFromLocal()
            return memoRepository.deleteAllMemos(memoQuery)
        }
    }
