package com.qure.data.datasource.auth

import com.qure.data.api.AuthService
import com.qure.data.entity.auth.PostSignupRequest
import com.qure.data.entity.auth.SignUpUserEntity
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(
    private val authService: AuthService
) : AuthRemoteDataSource {

    override suspend fun postSignUp(email: String, userId: String): Result<SignUpUserEntity> {
        return authService.postSignUp(PostSignupRequest(email, userId))
    }
}