package com.qure.data.entity.auth

data class PostAuthDatabaseRequest(
    val name: String,
    val fields: FieldsEntity,
    val createTime: String,
    val updateTime: String,
)