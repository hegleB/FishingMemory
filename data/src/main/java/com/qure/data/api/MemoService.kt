package com.qure.data.api

import com.qure.data.entity.memo.MemoEntity
import com.qure.data.entity.memo.MemoStorageEntity
import com.qure.domain.entity.memo.MemoFieldsEntity
import com.qure.domain.entity.memo.MemoImage
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface MemoService {

    @POST("/v1beta1/projects/{projectId}/databases/(default)/documents/memo")
    suspend fun postMemo(
        @Path("projectId") projectId: String,
        @Body fields: MemoFieldsEntity,
    ): Result<MemoEntity>
}