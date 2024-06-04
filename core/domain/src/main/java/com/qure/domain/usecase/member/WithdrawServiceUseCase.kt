package com.qure.domain.usecase.member

import com.qure.data.repository.auth.AuthRepository
import com.qure.data.repository.memo.MemoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WithdrawServiceUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository,
        private val memoRepository: MemoRepository,
    ) {
        operator fun invoke(): Flow<Boolean> {
            authRepository.removeTokenFromLocal()
            authRepository.removeTokenFromLocal()
            return memoRepository.deleteAllMemos()
        }
    }
