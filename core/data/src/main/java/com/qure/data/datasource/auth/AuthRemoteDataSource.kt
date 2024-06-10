package com.qure.data.datasource.auth

import com.qure.data.entity.auth.SignUpUserEntity

internal interface AuthRemoteDataSource {
    suspend fun postSignUp(
        email: String,
        userId: String,
    ): SignUpUserEntity

    suspend fun getSignedUpUser(email: String): SignUpUserEntity

    suspend fun deleteUserEmail(email: String)
}
