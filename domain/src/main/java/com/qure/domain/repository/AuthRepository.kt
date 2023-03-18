package com.qure.domain.repository

import com.qure.domain.entity.auth.SignUpUser

interface AuthRepository {
    suspend fun createUser(email: String, socialToken: String): Result<SignUpUser>
    fun saveTokenToLocal(token: String)
    fun getAccessTokenFromLocal() :String
}