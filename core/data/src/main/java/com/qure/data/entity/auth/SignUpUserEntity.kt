package com.qure.data.entity.auth

import com.qure.model.auth.SignUpFields
import kotlinx.serialization.Serializable

@Serializable
data class SignUpUserEntity(
    val name: String,
    val fields: SignUpFields,
    val createTime: String,
    val updateTime: String,
)
