package com.qure.domain.usecase.member

import com.qure.domain.entity.memo.MemoQuery
import com.qure.domain.repository.AuthRepository
import javax.inject.Inject

class WithdrawServiceUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {

    operator suspend fun invoke(memoQuery: MemoQuery) {
        authRepository.removeTokenFromLocal()
        authRepository.removeTokenFromLocal()
        authRepository.withdrawService(memoQuery)
    }
}