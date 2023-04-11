package com.qure.data.repository

import com.qure.data.datasource.memo.MemoRemoteDataSource
import com.qure.data.datasource.memo.MemoStorageRemoteDataSource
import com.qure.data.mapper.toMemo
import com.qure.data.mapper.toMemoStorage
import com.qure.domain.entity.memo.Memo
import com.qure.domain.entity.memo.MemoFields
import com.qure.domain.entity.memo.MemoStorage
import com.qure.domain.repository.MemoRepository
import java.io.File
import javax.inject.Inject

class MemoRepositoryImpl @Inject constructor(
    private val memoRemoteDataSource: MemoRemoteDataSource,
    private val memoStorageRemoteDataSource: MemoStorageRemoteDataSource,
): MemoRepository {

    override suspend fun createMemo(memoFields: MemoFields): Result<Memo> {
        return memoRemoteDataSource.postMemo(memoFields).map { it.toMemo() }
    }

    override suspend fun uploadMemoImage(image: File): Result<MemoStorage> {
        return memoStorageRemoteDataSource.postMemoStorage(image).map { it.toMemoStorage() }
    }
}