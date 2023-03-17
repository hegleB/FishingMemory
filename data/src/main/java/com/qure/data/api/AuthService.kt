package com.qure.data.api

import com.qure.data.entity.auth.PostSignupRequest
import com.qure.data.entity.auth.SignUpUserEntity
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("/v1/accounts:signUp")
    suspend fun postSignUp(
        @Body request: PostSignupRequest
    ): Result<SignUpUserEntity>
}