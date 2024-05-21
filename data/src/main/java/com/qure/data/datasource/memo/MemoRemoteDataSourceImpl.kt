package com.qure.data.datasource.memo

import com.qure.build_property.BuildProperty
import com.qure.build_property.BuildPropertyRepository
import com.qure.data.api.MemoService
import com.qure.data.entity.memo.MemoEntity
import com.qure.data.entity.memo.MemoQueryEntity
import com.qure.data.entity.memo.UpdatedMemoEntity
import com.qure.domain.entity.memo.MemoFields
import com.qure.domain.entity.memo.MemoFieldsEntity
import com.qure.domain.entity.memo.MemoQuery
import javax.inject.Inject

class MemoRemoteDataSourceImpl
    @Inject
    constructor(
        private val memoService: MemoService,
        private val buildPropertyRepository: BuildPropertyRepository,
    ) : MemoRemoteDataSource {
        override suspend fun postMemo(memoFields: MemoFields): MemoEntity {
            return memoService.postMemo(
                buildPropertyRepository.get(BuildProperty.FIREBASE_DATABASE_PROJECT_ID),
                memoFields.uuid.stringValue,
                MemoFieldsEntity(memoFields),
            )
        }

        override suspend fun postMemoQuery(memoQuery: MemoQuery): List<MemoQueryEntity> {
            return memoService.postMemoFiltering(
                buildPropertyRepository.get(BuildProperty.FIREBASE_DATABASE_PROJECT_ID),
                memoQuery,
            )
        }

        override suspend fun deleteMemo(uuid: String) {
            return memoService.deleteMemo(
                buildPropertyRepository.get(BuildProperty.FIREBASE_DATABASE_PROJECT_ID),
                uuid,
            )
        }

        override suspend fun updateMemo(memoFields: MemoFields): UpdatedMemoEntity {
            return memoService.updateMemo(
                buildPropertyRepository.get(BuildProperty.FIREBASE_DATABASE_PROJECT_ID),
                memoFields.uuid.stringValue,
                MemoFieldsEntity(memoFields),
            )
        }
    }
