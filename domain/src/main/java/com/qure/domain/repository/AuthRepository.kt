package com.qure.domain.repository

import com.qure.domain.entity.auth.SignUpUser
import com.qure.domain.entity.auth.Token
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun createUser(email: String, userId: String): Result<SignUpUser>
}