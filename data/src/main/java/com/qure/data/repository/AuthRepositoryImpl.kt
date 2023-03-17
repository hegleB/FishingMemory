package com.qure.data.repository

import android.content.SharedPreferences
import com.qure.data.datasource.FishMemorySharedPreference
import com.qure.data.datasource.auth.AuthRemoteDataSource
import com.qure.data.mapper.toSignUpUser
import com.qure.domain.ACCESS_TOKEN_KEY
import com.qure.domain.SHARED_PREFERNCE_KEY
import com.qure.domain.entity.auth.SignUpUser
import com.qure.domain.entity.auth.Token
import com.qure.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val fishingMemorySharedPreference: FishMemorySharedPreference,
) : AuthRepository {

    override suspend fun createUser(email: String, userId: String): Result<SignUpUser> {
        return authRemoteDataSource.postSignUp(email, userId).map { it.toSignUpUser() }
    }

    override fun saveTokenToLocal(token: String) {
        fishingMemorySharedPreference.putString(ACCESS_TOKEN_KEY, token)
    }

    override fun getAccessTokenFromLocal(): String {
        return fishingMemorySharedPreference.getString(ACCESS_TOKEN_KEY)
    }
}