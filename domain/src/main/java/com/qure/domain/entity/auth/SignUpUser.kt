package com.qure.domain.entity.auth

data class SignUpUser(
    val kind: String,
    val email: String,
    val token: Token,
    val expiresIn: String,
    val localId: String,
    val isSignUp: Boolean,
)