package com.qure.data.entity.memo

import com.qure.domain.entity.memo.MemoFieldsEntity

data class MemoEntity(
    val name: String,
    val MemoFields: MemoFieldsEntity,
    val createTime: String,
    val updateTime: String,
)