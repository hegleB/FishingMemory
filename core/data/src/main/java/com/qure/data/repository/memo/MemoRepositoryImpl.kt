package com.qure.data.repository.memo

import com.qure.data.datasource.memo.MemoLocalDataSource
import com.qure.data.datasource.memo.MemoRemoteDataSource
import com.qure.data.datasource.memo.MemoStorageRemoteDataSource
import com.qure.data.mapper.toDocument
import com.qure.data.mapper.toMemo
import com.qure.data.mapper.toMemoLocalEntity
import com.qure.data.mapper.toMemoStorage
import com.qure.data.mapper.toMemosLocalEntity
import com.qure.data.utils.NetworkMonitor
import com.qure.model.memo.Document
import com.qure.model.memo.Memo
import com.qure.model.memo.MemoFields
import com.qure.model.memo.MemoStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.single
import java.io.File
import javax.inject.Inject

internal class MemoRepositoryImpl
@Inject
constructor(
    private val memoRemoteDataSource: MemoRemoteDataSource,
    private val memoStorageRemoteDataSource: MemoStorageRemoteDataSource,
    private val memoLocalDataSource: MemoLocalDataSource,
    private val networkMonitor: NetworkMonitor,
) : MemoRepository {

    override suspend fun createMemo(memoFields: MemoFields): Memo {
        return flow { emit(memoRemoteDataSource.postMemo(memoFields)) }
            .onEach {
                if (memoFields.image.stringValue.isNotBlank()) {
                    memoLocalDataSource.insertMemo(memoFields.toMemoLocalEntity())
                }
            }
            .single()
            .toMemo()
    }

    override suspend fun uploadMemoImage(image: File): MemoStorage {
        return memoStorageRemoteDataSource.postMemoStorage(image).toMemoStorage()
    }

    override fun getUpdatedMemo(memoFields: MemoFields): Flow<Document> {
        return flow { emit(memoRemoteDataSource.updateMemo(memoFields).toDocument()) }
            .onEach {
                memoLocalDataSource.updateMemo(memoFields.toMemoLocalEntity())
            }
    }

    override fun deleteMemo(uuid: String): Flow<Unit> {
        return flow { emit(memoRemoteDataSource.deleteMemo(uuid)) }
            .onEach {
                memoLocalDataSource.deleteMemo(uuid)
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getMemos(): Flow<List<Memo>> {
        return networkMonitor.isConnectNetwork.flatMapLatest { isConnectNetwork ->
            if (isConnectNetwork) {
                fetchMemosFromRemoteOrLocal()
            } else {
                fetchMemosFromLocal()
            }
        }
    }

    private fun fetchMemosFromRemoteOrLocal(): Flow<List<Memo>> = flow {
        val localMemos = memoLocalDataSource.getMemos()
        if (localMemos.isEmpty()) {
            val remoteMemos = memoRemoteDataSource.postMemoQuery()
                .map { it.toMemo() }
                .filter { it.createTime.isNotEmpty() }

            emit(remoteMemos)
            memoLocalDataSource.insertMemos(remoteMemos.toMemosLocalEntity())
        } else {
            emit(localMemos.map { it.toMemo() })
        }
    }

    private fun fetchMemosFromLocal(): Flow<List<Memo>> = flow {
        val localMemos = memoLocalDataSource.getMemos()
        emit(localMemos.map { it.toMemo() })
    }

    override fun deleteAllMemos(): Flow<Boolean> {
        return flow {
            emit(memoRemoteDataSource.postMemoQuery().isEmpty())
        }
    }
}
