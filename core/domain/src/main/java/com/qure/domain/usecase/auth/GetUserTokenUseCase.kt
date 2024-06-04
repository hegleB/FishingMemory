package com.qure.domain.usecase.auth

import com.qure.data.repository.auth.AuthRepository
import com.qure.model.auth.SignUpUser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserTokenUseCase
    @Inject
    constructor(
        private val authRepository: AuthRepository,
    ) {
        operator fun invoke(email: String): Flow<SignUpUser> = authRepository.getSignedUpUser(email)
    }
