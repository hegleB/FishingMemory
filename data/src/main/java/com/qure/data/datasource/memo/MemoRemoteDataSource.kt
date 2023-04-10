package com.qure.data.datasource.memo

import com.qure.data.entity.memo.MemoEntity
import com.qure.domain.entity.memo.MemoFields

interface MemoRemoteDataSource {
    suspend fun postMemo(memoFields: MemoFields): Result<MemoEntity>
}