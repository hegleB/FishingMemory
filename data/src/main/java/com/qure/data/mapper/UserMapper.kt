package com.qure.data.mapper

import com.qure.data.entity.auth.SignUpUserEntity
import com.qure.domain.entity.auth.SignUpUser

fun SignUpUserEntity.toSignUpUser(): SignUpUser {
    val data = this

    return SignUpUser(
        kind = data.kind,
        email = data.email,
        idToken = data.idToken,
        expiresIn = data.expiresIn,
        localId = data.localId,
        isSignUp = true,
    )
}