package com.qure.data.datasource.auth

import com.qure.build_property.BuildProperty
import com.qure.build_property.BuildPropertyRepository
import com.qure.data.api.AuthService
import com.qure.data.api.MemoService
import com.qure.data.entity.auth.SignUpUserEntity
import com.qure.domain.entity.auth.Email
import com.qure.domain.entity.auth.SignUpFields
import com.qure.domain.entity.auth.SignUpFieldsEntity
import com.qure.domain.entity.auth.Token
import javax.inject.Inject

class AuthRemoteDataSourceImpl
    @Inject
    constructor(
        private val authService: AuthService,
        private val buildPropertyRepository: BuildPropertyRepository,
        private val memoService: MemoService,
    ) : AuthRemoteDataSource {
        override suspend fun postSignUp(
            email: String,
            socialToken: String,
        ): Result<SignUpUserEntity> {
            return authService.postSignUp(
                buildPropertyRepository.get(BuildProperty.FIREBASE_DATABASE_PROJECT_ID),
                email,
                SignUpFieldsEntity(SignUpFields(Email(email), Token(socialToken))),
            )
        }

        override suspend fun getSignedUpUser(email: String): Result<SignUpUserEntity> {
            return authService.getUserInfo(
                buildPropertyRepository.get(BuildProperty.FIREBASE_DATABASE_PROJECT_ID),
                email,
            )
        }

        override suspend fun deleteUserEmail(email: String): Result<Unit> {
            return authService.deleteUserEmail(
                buildPropertyRepository.get(BuildProperty.FIREBASE_DATABASE_PROJECT_ID),
                email,
            )
        }
    }
