package com.qure.data.entity.auth

import com.qure.domain.entity.auth.SignUpFields

data class SignUpUserEntity(
    val name: String,
    val fields: SignUpFields,
    val createTime: String,
    val updateTime: String,
)