package com.qure.domain.usecase.auth

import com.qure.domain.entity.auth.SignUpUser
import com.qure.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, userId: String): Result<SignUpUser> {
        return authRepository.createUser(email, userId)
    }
}