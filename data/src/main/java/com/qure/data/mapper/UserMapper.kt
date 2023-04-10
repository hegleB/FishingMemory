package com.qure.data.mapper

import com.qure.data.entity.auth.*
import com.qure.domain.entity.auth.SignUpUser

fun SignUpUserEntity.toSignUpUser(): SignUpUser {
    val data = this

    return SignUpUser(
        name = data.name,
        fields = data.signUpFields,
        createTime = data.createTime,
        updateTime = data.updateTime,
    )
}