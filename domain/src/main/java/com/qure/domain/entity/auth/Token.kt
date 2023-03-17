package com.qure.domain.entity.auth

data class Token(
    val idToken: String,
    val refreshToken: String,
)