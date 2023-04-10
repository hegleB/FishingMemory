package com.qure.domain.entity.auth

data class SignUpUser(
    val name: String,
    val fields: SignUpFields,
    val createTime: String,
    val updateTime: String,
)

data class SignUpFieldsEntity(
    val fields: SignUpFields
)

data class SignUpFields(
    val email: Email,
    val token: Token,
)

data class Email(
    val stringValue: String,
)

data class Token(
    val stringValue: String,
)