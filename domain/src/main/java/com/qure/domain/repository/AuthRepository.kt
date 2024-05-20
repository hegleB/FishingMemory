package com.qure.domain.repository

import com.qure.domain.entity.auth.SignUpUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun createUser(
        email: String,
        socialToken: String,
    ): SignUpUser

    fun saveTokenToLocal(token: String)

    fun saveEmailToLocal(email: String)

    fun removeTokenFromLocal()

    fun removeEmailFromLocal()

    fun getAccessTokenFromLocal(): String

    fun getEmailFromLocal(): String

    fun getSignedUpUser(email: String): Flow<SignUpUser>

    fun removeEmailFromRemote(email: String): Flow<Unit>
}
