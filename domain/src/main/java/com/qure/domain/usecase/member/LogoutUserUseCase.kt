package com.qure.domain.usecase.member


import com.qure.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

import javax.inject.Inject

class LogoutUserUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    operator fun invoke(email: String): Flow<Result<Unit>> {
        authRepository.removeTokenFromLocal()
        authRepository.removeEmailFromLocal()
        return authRepository.removeEmailFromRemote(email)
    }
}