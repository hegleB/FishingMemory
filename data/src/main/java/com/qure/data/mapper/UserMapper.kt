package com.qure.data.mapper

import com.qure.data.entity.auth.SignUpUserEntity
import com.qure.data.entity.base.ApiResponse
import com.qure.domain.entity.auth.SignUpUser

fun ApiResponse<SignUpUserEntity>.toSignUpUser(): SignUpUser {
    val data = this.data ?: throw IllegalStateException()
    val code = this.code
    return SignUpUser(
        kind = data.kind,
        email = data.email,
        token = data.token,
        expiresIn = data.expiresIn,
        localId = data.localId,
        isSignUp = true,
    )
}