package com.qure.data.repository.memo

import com.qure.data.datasource.memo.MemoLocalDataSource
import com.qure.data.datasource.memo.MemoRemoteDataSource
import com.qure.data.datasource.memo.MemoStorageRemoteDataSource
import com.qure.data.mapper.toDocument
import com.qure.data.mapper.toMemo
import com.qure.data.mapper.toMemoLocalEntity
import com.qure.data.mapper.toMemoStorage
import com.qure.data.mapper.toMemosLocalEntity
import com.qure.model.memo.Document
import com.qure.model.memo.Memo
import com.qure.model.memo.MemoFields
import com.qure.model.memo.MemoStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

internal class MemoRepositoryImpl
@Inject
constructor(
    private val memoRemoteDataSource: MemoRemoteDataSource,
    private val memoStorageRemoteDataSource: MemoStorageRemoteDataSource,
    private val memoLocalDataSource: MemoLocalDataSource,
) : MemoRepository {

    override suspend fun createMemo(memoFields: MemoFields): Memo {
        if (memoFields.image.stringValue.isNotBlank()) {
            memoLocalDataSource.insertMemo(memoFields.toMemoLocalEntity())
        }
        return memoRemoteDataSource.postMemo(memoFields).toMemo()
    }

    override suspend fun uploadMemoImage(image: File): MemoStorage {
        return memoStorageRemoteDataSource.postMemoStorage(image).toMemoStorage()
    }

    override fun getUpdatedMemo(memoFields: MemoFields): Flow<Document> {
        return flow {
            memoLocalDataSource.updateMemo(memoFields.toMemoLocalEntity())
            emit(memoRemoteDataSource.updateMemo(memoFields).toDocument())
        }
    }

    override fun deleteMemo(uuid: String): Flow<Unit> {
        return flow {
            memoLocalDataSource.deleteMemo(uuid)
            emit(memoRemoteDataSource.deleteMemo(uuid))
        }
    }

    override fun getMemos(): Flow<List<Memo>> {
        return flow {
            val memos = memoLocalDataSource.getMemos()
            if (memos.isEmpty()) {
                val remoteMemos = memoRemoteDataSource.postMemoQuery()
                    .map { it.toMemo() }
                    .filter { it.createTime != "" }
                emit(remoteMemos)
                memoLocalDataSource.insertMemos(remoteMemos.toMemosLocalEntity())
            } else {
                emit(memos.map { it.toMemo() })
            }
        }
    }

    override fun deleteAllMemos(): Flow<Boolean> {
        return flow {
            emit(memoRemoteDataSource.postMemoQuery().isEmpty())
        }
    }
}
