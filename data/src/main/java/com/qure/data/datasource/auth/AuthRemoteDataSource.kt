package com.qure.data.datasource.auth

import com.qure.data.entity.auth.SignUpUserEntity
import com.qure.domain.entity.memo.MemoQuery
import kotlinx.coroutines.flow.Flow

interface AuthRemoteDataSource {
    suspend fun postSignUp(email: String, userId: String): Result<SignUpUserEntity>
    suspend fun getSignedUpUser(email: String): Result<SignUpUserEntity>
    suspend fun deleteUserEmail(email: String): Result<Unit>
}