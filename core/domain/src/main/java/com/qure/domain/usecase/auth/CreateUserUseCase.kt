package com.qure.domain.usecase.auth

import com.qure.data.repository.auth.AuthRepository
import com.qure.model.auth.SignUpUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreateUserUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository,
    ) {
        operator fun invoke(
            email: String,
            socialToken: String,
        ): Flow<SignUpUser> {
            return flow {
                emit(authRepository.createUser(email, socialToken))
            }
        }
    }
