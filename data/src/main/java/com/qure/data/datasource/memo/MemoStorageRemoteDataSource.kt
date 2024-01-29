package com.qure.data.datasource.memo

import com.qure.data.entity.memo.MemoStorageEntity
import java.io.File

interface MemoStorageRemoteDataSource {
    suspend fun postMemoStorage(image: File): Result<MemoStorageEntity>
}
