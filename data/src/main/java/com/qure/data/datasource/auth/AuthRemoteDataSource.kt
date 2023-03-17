package com.qure.data.datasource.auth

import com.qure.data.entity.auth.SignUpUserEntity
import com.qure.data.entity.base.ApiResponse
import retrofit2.Call
import retrofit2.Response

interface AuthRemoteDataSource {
    suspend fun postSignUp(email: String, userId: String): Result<ApiResponse<SignUpUserEntity>>
}