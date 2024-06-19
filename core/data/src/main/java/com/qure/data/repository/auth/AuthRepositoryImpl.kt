package com.qure.data.repository.auth

import com.qure.data.datasource.FishMemorySharedPreference
import com.qure.data.datasource.auth.AuthRemoteDataSource
import com.qure.data.mapper.toSignUpUser
import com.qure.data.utils.ACCESS_TOKEN_KEY
import com.qure.data.utils.SIGNED_UP_EMAIL
import com.qure.model.auth.SignUpUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

internal class AuthRepositoryImpl
@Inject
constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val fishingMemorySharedPreference: FishMemorySharedPreference,
) : AuthRepository {
    override suspend fun createUser(
        email: String,
        socialToken: String,
    ): SignUpUser {
        return authRemoteDataSource.postSignUp(email, socialToken).toSignUpUser()
    }

    override fun saveTokenToLocal(token: String) {
        fishingMemorySharedPreference.putString(ACCESS_TOKEN_KEY, token)
    }

    override fun saveEmailToLocal(email: String) {
        fishingMemorySharedPreference.putString(SIGNED_UP_EMAIL, email)
    }

    override fun removeTokenFromLocal() {
        fishingMemorySharedPreference.remove(ACCESS_TOKEN_KEY)
    }

    override fun removeEmailFromLocal() {
        fishingMemorySharedPreference.remove(SIGNED_UP_EMAIL)
    }

    override fun getAccessTokenFromLocal(): String {
        return fishingMemorySharedPreference.getString(ACCESS_TOKEN_KEY)
    }

    override fun getEmailFromLocal(): String {
        return fishingMemorySharedPreference.getString(SIGNED_UP_EMAIL)
    }

    override fun getSignedUpUser(email: String): Flow<SignUpUser> =
        flow {
            emit(authRemoteDataSource.getSignedUpUser(email).toSignUpUser())
        }

    override fun removeEmailFromRemote(email: String): Flow<Unit> {
        return flow { emit(authRemoteDataSource.deleteUserEmail(email)) }
            .onEach {
                removeEmailFromLocal()
                removeTokenFromLocal()
            }
    }
}
