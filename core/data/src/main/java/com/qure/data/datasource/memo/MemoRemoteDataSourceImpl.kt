package com.qure.data.datasource.memo

import com.qure.build_property.BuildProperty
import com.qure.build_property.BuildPropertyRepository
import com.qure.data.api.MemoService
import com.qure.data.datasource.FishMemorySharedPreference
import com.qure.data.entity.memo.MemoEntity
import com.qure.data.entity.memo.MemoQueryEntity
import com.qure.data.utils.SIGNED_UP_EMAIL
import com.qure.model.memo.CollectionId
import com.qure.model.memo.CompositeFilter
import com.qure.model.memo.FieldFilter
import com.qure.model.memo.FieldPath
import com.qure.model.memo.Filter
import com.qure.model.memo.MemoFields
import com.qure.model.memo.MemoFieldsEntity
import com.qure.model.memo.MemoQuery
import com.qure.model.memo.OrderBy
import com.qure.model.memo.StructuredQuery
import com.qure.model.memo.Value
import com.qure.model.memo.Where
import javax.inject.Inject

internal class MemoRemoteDataSourceImpl
@Inject
constructor(
    private val memoService: MemoService,
    private val buildPropertyRepository: BuildPropertyRepository,
    private val fishingMemorySharedPreference: FishMemorySharedPreference,
) : MemoRemoteDataSource {
    override suspend fun postMemo(memoFields: MemoFields): MemoEntity {
        return memoService.postMemo(
            buildPropertyRepository.get(BuildProperty.FIREBASE_DATABASE_PROJECT_ID),
            memoFields.uuid.stringValue,
            MemoFieldsEntity(memoFields),
        )
    }

    override suspend fun postMemoQuery(): List<MemoQueryEntity> {
        return memoService.postMemoFiltering(
            buildPropertyRepository.get(BuildProperty.FIREBASE_DATABASE_PROJECT_ID),
            getMemoQuery(),
        )
    }

    override suspend fun deleteMemo(uuid: String) {
        return memoService.deleteMemo(
            buildPropertyRepository.get(BuildProperty.FIREBASE_DATABASE_PROJECT_ID),
            uuid,
        )
    }

    override suspend fun updateMemo(memoFields: MemoFields): MemoEntity {
        return memoService.updateMemo(
            buildPropertyRepository.get(BuildProperty.FIREBASE_DATABASE_PROJECT_ID),
            memoFields.uuid.stringValue,
            MemoFieldsEntity(memoFields),
        )
    }

    private fun getMemoQuery(): MemoQuery {
        val emailFilter =
            FieldFilter(
                field = FieldPath(EMAIL),
                op = EQUAL,
                value = Value(
                    fishingMemorySharedPreference.getString(SIGNED_UP_EMAIL)
                ),
            )

        val compositeFilter =
            CompositeFilter(
                op = AND,
                filters = listOf(Filter(emailFilter)),
            )

        return MemoQuery(
            StructuredQuery(
                from = listOf(CollectionId(COLLECTION_ID)),
                where = Where(compositeFilter),
                orderBy = listOf(OrderBy(FieldPath(DATE), DESCENDING)),
            ),
        )
    }

    companion object {
        private const val EMAIL = "email"
        private const val DATE = "date"
        private const val DESCENDING = "DESCENDING"
        private const val EQUAL = "EQUAL"
        private const val AND = "AND"
        private const val COLLECTION_ID = "memo"
    }
}
