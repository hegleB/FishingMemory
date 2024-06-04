package com.qure.model.auth

import kotlinx.serialization.Serializable

data class SignUpUser(
    val name: String,
    val fields: SignUpFields,
    val createTime: String,
    val updateTime: String,
)

@Serializable
data class SignUpFieldsEntity(
    val fields: SignUpFields,
)

@Serializable
data class SignUpFields(
    val email: Email,
    val token: Token,
)

@Serializable
data class Email(
    val stringValue: String,
)

@Serializable
data class Token(
    val stringValue: String,
)
