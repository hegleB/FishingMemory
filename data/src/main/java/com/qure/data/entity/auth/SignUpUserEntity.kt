package com.qure.data.entity.auth

import com.qure.domain.entity.auth.Fields

data class SignUpUserEntity(
    val name: String,
    val fields: Fields,
    val createTime: String,
    val updateTime: String,
)