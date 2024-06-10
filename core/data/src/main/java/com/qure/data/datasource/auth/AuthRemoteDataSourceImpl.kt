package com.qure.data.datasource.auth

import com.qure.build_property.BuildProperty
import com.qure.build_property.BuildPropertyRepository
import com.qure.data.api.AuthService
import com.qure.data.entity.auth.SignUpUserEntity
import com.qure.model.auth.Email
import com.qure.model.auth.SignUpFields
import com.qure.model.auth.SignUpFieldsEntity
import com.qure.model.auth.Token
import javax.inject.Inject

internal  class AuthRemoteDataSourceImpl
    @Inject
    constructor(
        private val authService: AuthService,
        private val buildPropertyRepository: BuildPropertyRepository,
    ) : AuthRemoteDataSource {
        override suspend fun postSignUp(
            email: String,
            socialToken: String,
        ): SignUpUserEntity {
            return authService.postSignUp(
                buildPropertyRepository.get(BuildProperty.FIREBASE_DATABASE_PROJECT_ID),
                email,
                SignUpFieldsEntity(SignUpFields(Email(email), Token(socialToken))),
            )
        }

        override suspend fun getSignedUpUser(email: String): SignUpUserEntity {
            return authService.getUserInfo(
                buildPropertyRepository.get(BuildProperty.FIREBASE_DATABASE_PROJECT_ID),
                email,
            )
        }

        override suspend fun deleteUserEmail(email: String) {
            return authService.deleteUserEmail(
                buildPropertyRepository.get(BuildProperty.FIREBASE_DATABASE_PROJECT_ID),
                email,
            )
        }
    }
