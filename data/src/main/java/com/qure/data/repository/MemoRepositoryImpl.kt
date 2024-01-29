package com.qure.data.repository

import com.qure.data.datasource.memo.MemoRemoteDataSource
import com.qure.data.datasource.memo.MemoStorageRemoteDataSource
import com.qure.data.mapper.toDocument
import com.qure.data.mapper.toMemo
import com.qure.data.mapper.toMemoStorage
import com.qure.domain.entity.memo.*
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
        override suspend fun createMemo(memoFields: MemoFields): Result<Memo> {
            return memoRemoteDataSource.postMemo(memoFields).map { it.toMemo() }
        }

        override suspend fun uploadMemoImage(image: File): Result<MemoStorage> {
            return memoStorageRemoteDataSource.postMemoStorage(image).map { it.toMemoStorage() }
        }

        override fun getUpdatedMemo(memoFields: MemoFields): Flow<Result<Document>> {
            return flow {
                memoRemoteDataSource.updateMemo(memoFields).map { it.toDocument() }
                    .onSuccess {
                        emit(Result.success(it))
                    }
                    .onFailure { throwable ->
                        emit(Result.failure(throwable))
                    }
            }
        }

        override fun deleteMemo(uuid: String): Flow<Result<Unit>> {
            return flow {
                memoRemoteDataSource.deleteMemo(uuid)
                    .onSuccess {
                        emit(Result.success(it))
                    }
                    .onFailure { throwable ->
                        emit(Result.failure(throwable))
                    }
            }
        }

        override fun getfilteredMemo(memoQuery: MemoQuery): Flow<Result<List<Memo>>> {
            return flow {
                memoRemoteDataSource.postMemoQuery(memoQuery)
                    .onSuccess { memo ->
                        val emptyMemoQueryEntity = memo.filter { it.document != null }
                        if (emptyMemoQueryEntity.isEmpty()) {
                            emit(Result.success(emptyList()))
                        } else {
                            emit(Result.success(memo.map { it.toMemo() }))
                        }
                    }
                    .onFailure { throwable ->
                        emit(Result.failure(throwable))
                    }
            }
        }

        override fun deleteAllMemos(memoQuery: MemoQuery): Flow<Result<Boolean>> {
            return flow {
                memoRemoteDataSource.postMemoQuery(memoQuery)
                    .onSuccess { memos ->
                        val filteredMemos = memos.filter { it.document != null }

                        if (filteredMemos.isNotEmpty()) {
                            for (memo in memos) {
                                val uuid = memo.document?.fields?.uuid?.stringValue ?: ""
                                memoRemoteDataSource.deleteMemo(uuid)
                                    .onSuccess {
                                        emit(Result.success(true))
                                    }.onFailure { throwable ->
                                        emit(Result.failure(throwable))
                                    }
                            }
                        } else {
                            emit(Result.success(true))
                        }
                    }.onFailure { throwable ->
                        emit(Result.failure(throwable))
                    }
            }
        }
    }
