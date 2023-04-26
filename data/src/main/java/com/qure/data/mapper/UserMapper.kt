package com.qure.data.mapper

import com.qure.data.entity.auth.SignUpUserEntity
import com.qure.domain.entity.auth.SignUpUser

fun SignUpUserEntity.toSignUpUser(): SignUpUser {
    val data = this

    return SignUpUser(
        name = data.name,
        fields = data.fields,
        createTime = data.createTime,
        updateTime = data.updateTime,
    )
}