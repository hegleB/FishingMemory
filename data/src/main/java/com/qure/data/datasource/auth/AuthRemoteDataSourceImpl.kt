package com.qure.data.datasource.auth

import com.qure.build_property.BuildProperty
import com.qure.build_property.BuildPropertyRepository
import com.qure.data.api.AuthService
import com.qure.data.entity.auth.*
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(
    private val authService: AuthService,
    private val buildPropertyRepository: BuildPropertyRepository,
) : AuthRemoteDataSource {

    override suspend fun postSignUp(email: String, socialToken: String): Result<SignUpUserEntity> {
        return authService.postSignUp(buildPropertyRepository.get(BuildProperty.FIREBASE_DATABASE_PROJECT_ID), email, FieldsEntity(FieldEntity(EmailEntity(email), TokenEntity(socialToken))))
    }
}