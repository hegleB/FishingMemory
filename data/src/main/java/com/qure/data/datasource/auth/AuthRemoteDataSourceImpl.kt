package com.qure.data.datasource.auth

import com.qure.build_property.BuildProperty
import com.qure.build_property.BuildPropertyRepository
import com.qure.data.api.AuthService
import com.qure.data.entity.auth.*
import com.qure.domain.entity.auth.Email
import com.qure.domain.entity.auth.SignUpFields
import com.qure.domain.entity.auth.Token
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(
    private val authService: AuthService,
    private val buildPropertyRepository: BuildPropertyRepository,
) : AuthRemoteDataSource {

    override suspend fun postSignUp(email: String, socialToken: String): Result<SignUpUserEntity> {
        return authService.postSignUp(buildPropertyRepository.get(BuildProperty.FIREBASE_DATABASE_PROJECT_ID), email, SignUpFields(Email(email), Token(socialToken)))
    }

    override suspend fun getSignedUpUser(email: String): Result<SignUpUserEntity> {
        return authService.getUserInfo(buildPropertyRepository.get(BuildProperty.FIREBASE_DATABASE_PROJECT_ID), email)
    }
}