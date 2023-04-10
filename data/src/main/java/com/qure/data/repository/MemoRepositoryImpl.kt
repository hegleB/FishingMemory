package com.qure.data.repository

import com.qure.data.datasource.memo.MemoRemoteDataSource
import com.qure.data.mapper.toMemo
import com.qure.domain.entity.memo.Memo
import com.qure.domain.entity.memo.MemoFields
import com.qure.domain.repository.MemoRepository
import javax.inject.Inject

class MemoRepositoryImpl @Inject constructor(
    private val memoRemoteDataSource: MemoRemoteDataSource
): MemoRepository {

    override suspend fun createMemo(memoFields: MemoFields): Result<Memo> {
        return memoRemoteDataSource.postMemo(memoFields).map { it.toMemo() }
    }
}