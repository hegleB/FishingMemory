package com.qure.data.repository

import com.qure.data.datasource.FishMemorySharedPreference
import com.qure.data.datasource.auth.AuthRemoteDataSource
import com.qure.data.mapper.toSignUpUser
import com.qure.domain.ACCESS_TOKEN_KEY
import com.qure.domain.entity.auth.SignUpUser
import com.qure.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val fishingMemorySharedPreference: FishMemorySharedPreference,
) : AuthRepository {

    override suspend fun createUser(email: String, socialToken: String): Result<SignUpUser> {
        return authRemoteDataSource.postSignUp(email, socialToken).map { it.toSignUpUser() }
    }

    override fun saveTokenToLocal(token: String) {
        fishingMemorySharedPreference.putString(ACCESS_TOKEN_KEY, token)
    }

    override fun removeTokenFromLocal() {
        fishingMemorySharedPreference.remove(ACCESS_TOKEN_KEY)
    }

    override fun getAccessTokenFromLocal(): String {
        return fishingMemorySharedPreference.getString(ACCESS_TOKEN_KEY)
    }

    override fun getSignedUpUser(email: String): Flow<Result<SignUpUser>> =
        flow {
            authRemoteDataSource.getSignedUpUser(email)
                .onSuccess { response ->
                    emit(Result.success(value = response.toSignUpUser()))
                }
                .onFailure { throwable ->
                    throwable as Exception
                    emit(Result.failure(throwable))
                }
        }
}