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
        fields = data.fields.toFields(),
        createTime = data.createTime,
        updateTime = data.updateTime,
    )
}

fun FieldEntity.toFields(): Fields {
    return Fields(
        email = this.email.toEmail(),
        token = this.token.toToken(),
    )
}

fun EmailEntity.toEmail(): Email {
    return Email(
        stringValue = this.stringValue
    )
}

fun TokenEntity.toToken(): Token {
    return Token(
        stringValue = this.stringValue
    )
}