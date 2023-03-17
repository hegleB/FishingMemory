package com.qure.data.entity.auth

import com.qure.domain.entity.auth.Token

data class SignUpUserEntity(
    val kind: String,
    val email: String,
    val token: Token,
    val expiresIn: String,
    val localId: String,
)