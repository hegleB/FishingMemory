package com.qure.data.datasource.auth

import com.qure.data.entity.auth.SignUpUserEntity

interface AuthRemoteDataSource {
    suspend fun postSignUp(email: String, userId: String): Result<SignUpUserEntity>
    suspend fun getSignedUpUser(email: String): Result<SignUpUserEntity>
}