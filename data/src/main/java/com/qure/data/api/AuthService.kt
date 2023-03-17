package com.qure.data.api

import com.qure.data.entity.auth.SignUpUserEntity
import com.qure.data.entity.base.ApiResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {

    @POST("/v1/accounts:signUp")
    @FormUrlEncoded
    suspend fun postSignUp(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Result<ApiResponse<SignUpUserEntity>>
}