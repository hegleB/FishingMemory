package com.qure.domain.usecase.auth

import com.qure.domain.entity.auth.SignUpUser
import com.qure.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(email: String): Flow<Result<SignUpUser>> = authRepository.getSignedUpUser(email)
}