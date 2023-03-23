package com.qure.data.mapper

import com.qure.data.entity.auth.*
import com.qure.domain.entity.auth.Email
import com.qure.domain.entity.auth.Fields
import com.qure.domain.entity.auth.SignUpUser
import com.qure.domain.entity.auth.Token

fun SignUpUserEntity.toSignUpUser(): SignUpUser {
    val data = this

    return SignUpUser(
        name = data.name,
        fields = data.fields,
        createTime = data.createTime,
        updateTime = data.updateTime,
    )
}