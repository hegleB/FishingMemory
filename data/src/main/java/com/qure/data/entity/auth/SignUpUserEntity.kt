package com.qure.data.entity.auth

data class SignUpUserEntity(
    val kind: String,
    val email: String,
    val idToken: String,
    val expiresIn: String,
    val localId: String,
)