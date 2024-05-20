package com.qure.data.repository

import com.qure.data.datasource.memo.MemoRemoteDataSource
import com.qure.data.datasource.memo.MemoStorageRemoteDataSource
import com.qure.data.mapper.toDocument
import com.qure.data.mapper.toMemo
import com.qure.data.mapper.toMemoStorage
import com.qure.domain.entity.memo.Document
import com.qure.domain.entity.memo.Memo
import com.qure.domain.entity.memo.MemoFields
import com.qure.domain.entity.memo.MemoQuery
import com.qure.domain.entity.memo.MemoStorage
import com.qure.domain.repository.MemoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

class MemoRepositoryImpl
@Inject
constructor(
    private val memoRemoteDataSource: MemoRemoteDataSource,
    private val memoStorageRemoteDataSource: MemoStorageRemoteDataSource,
) : MemoRepository {
    override suspend fun createMemo(memoFields: MemoFields): Memo {
        return memoRemoteDataSource.postMemo(memoFields).toMemo()
    }

    override suspend fun uploadMemoImage(image: File): MemoStorage {
        return memoStorageRemoteDataSource.postMemoStorage(image).toMemoStorage()
    }

    override fun getUpdatedMemo(memoFields: MemoFields): Flow<Document> {
        return flow {
            memoRemoteDataSource.updateMemo(memoFields).toDocument()
        }
    }

    override fun deleteMemo(uuid: String): Flow<Unit> {
        return flow {
            memoRemoteDataSource.deleteMemo(uuid)
        }
    }

    override fun getfilteredMemo(memoQuery: MemoQuery): Flow<List<Memo>> {
        return flow {
            memoRemoteDataSource.postMemoQuery(memoQuery)
        }
    }

    override fun deleteAllMemos(memoQuery: MemoQuery): Flow<Boolean> {
        return flow {
            memoRemoteDataSource.postMemoQuery(memoQuery)
        }
    }
}
