package com.qure.data.datasource.memo

import com.qure.data.entity.memo.MemoLocalEntity

internal interface MemoLocalDataSource {
    suspend fun insertMemo(memoLocalEntity: MemoLocalEntity)

    suspend fun insertMemos(memosLocalEntity: List<MemoLocalEntity>)

    suspend fun getMemos(): List<MemoLocalEntity>

    suspend fun deleteMemo(uuid: String)

    suspend fun deleteAllMemos()

    suspend fun updateMemo(memoLocalEntity: MemoLocalEntity)
}