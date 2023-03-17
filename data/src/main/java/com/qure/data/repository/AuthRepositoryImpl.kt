package com.qure.data.repository

import com.qure.data.datasource.auth.AuthRemoteDataSource
import com.qure.data.mapper.toSignUpUser
import com.qure.domain.entity.auth.SignUpUser
import com.qure.domain.repository.AuthRepository
import retrofit2.Response
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource
) : AuthRepository {

    override suspend fun createUser(email: String, userId: String): Result<SignUpUser> {
        return authRemoteDataSource.postSignUp(email, userId).map { it.toSignUpUser() }
    }
}