package com.qure.data.datasource.memo

import com.qure.build_property.BuildProperty
import com.qure.build_property.BuildPropertyRepository
import com.qure.data.api.MemoService
import com.qure.data.entity.memo.MemoEntity
import com.qure.data.entity.memo.MemoQueryEntity
import com.qure.domain.entity.memo.*
import javax.inject.Inject

class MemoRemoteDataSourceImpl @Inject constructor(
    private val memoService: MemoService,
    private val buildPropertyRepository: BuildPropertyRepository,
) : MemoRemoteDataSource {
    override suspend fun postMemo(memoFields: MemoFields): Result<MemoEntity> {
        return memoService.postMemo(
            buildPropertyRepository.get(BuildProperty.FIREBASE_DATABASE_PROJECT_ID),
            memoFields.uuid.stringValue,
            MemoFieldsEntity(memoFields)
        )
    }

    override suspend fun postMemoQuery(memoQuery: MemoQuery): Result<List<MemoQueryEntity>> {
        return memoService.postMemoFiltering(
            buildPropertyRepository.get(BuildProperty.FIREBASE_DATABASE_PROJECT_ID),
            memoQuery
        )
    }
}