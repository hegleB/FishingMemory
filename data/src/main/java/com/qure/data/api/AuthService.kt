package com.qure.data.api

import com.qure.data.entity.auth.SignUpUserEntity
import com.qure.domain.entity.auth.SignUpFieldsEntity
import retrofit2.http.*

interface AuthService {
    @POST("/v1beta1/projects/{projectId}/databases/(default)/documents/auth")
    suspend fun postSignUp(
        @Path("projectId") projectId: String,
        @Query("documentId") email: String,
        @Body fields: SignUpFieldsEntity,
    ): Result<SignUpUserEntity>

    @GET("/v1beta1/projects/{projectId}/databases/(default)/documents/auth/{documentId}")
    suspend fun getUserInfo(
        @Path("projectId") projectId: String,
        @Path("documentId") email: String,
    ): Result<SignUpUserEntity>

    @DELETE("/v1beta1/projects/{projectId}/databases/(default)/documents/auth/{documentId}")
    suspend fun deleteUserEmail(
        @Path("projectId") projectId: String,
        @Path("documentId") documentId: String,
    ): Result<Unit>
}
