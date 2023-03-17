package com.qure.data.datasource.auth

import com.qure.data.api.AuthService
import com.qure.data.entity.auth.SignUpUserEntity
import com.qure.data.entity.base.ApiResponse
import com.qure.data.entity.base.getResult
import retrofit2.Call
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(
    private val authService: AuthService
) : AuthRemoteDataSource {

    override suspend fun postSignUp(email: String, userId: String): Result<ApiResponse<SignUpUserEntity>> {
        return authService.postSignUp(email, userId)
    }
}