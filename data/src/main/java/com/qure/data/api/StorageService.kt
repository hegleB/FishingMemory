package com.qure.data.api

import com.qure.data.entity.memo.MemoStorageEntity
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface StorageService {
    @Multipart
    @POST("o/memo_image%2F{imageName}")
    suspend fun postMemoImage(
        @Path("imageName") memoImage: String,
        @Part image: MultipartBody.Part,
    ): Result<MemoStorageEntity>
}
