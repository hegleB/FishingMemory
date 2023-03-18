package com.qure.data.entity.auth

data class SignUpUserEntity(
    val name: String,
    val fields: FieldEntity,
    val createTime: String,
    val updateTime: String,
)

data class FieldsEntity(
    val fields: FieldEntity,
)

data class FieldEntity(
    val email: EmailEntity,
    val token: TokenEntity,
)

data class EmailEntity(
    val stringValue: String
)

data class TokenEntity(
    val stringValue: String
)