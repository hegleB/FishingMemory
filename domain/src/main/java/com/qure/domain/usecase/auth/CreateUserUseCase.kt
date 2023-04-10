package com.qure.domain.usecase.auth

import com.qure.domain.entity.auth.SignUpUser
import com.qure.domain.repository.AuthRepository
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(email: String, socialToken: String): Result<SignUpUser> {
        return authRepository.createUser(email, socialToken)
            .onSuccess {
                authRepository.saveTokenToLocal(it.fields.token.stringValue)
                authRepository.saveEmailToLocal(email)
            }.onFailure {
                throw it as Exception
            }
    }
}