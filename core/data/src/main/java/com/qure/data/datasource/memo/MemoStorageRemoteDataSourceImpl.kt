package com.qure.data.datasource.memo

import com.qure.data.api.StorageService
import com.qure.data.entity.memo.MemoStorageEntity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class MemoStorageRemoteDataSourceImpl
    @Inject
    constructor(
        private val storageService: StorageService,
    ) : MemoStorageRemoteDataSource {
        override suspend fun postMemoStorage(image: File): MemoStorageEntity {
            val profileImage: RequestBody =
                image.asRequestBody("image/jpg".toMediaTypeOrNull())

            val profileImageBody: MultipartBody.Part =
                MultipartBody.Part.createFormData(
                    "file",
                    image.name,
                    profileImage,
                )

            return storageService.postMemoImage(image.name, profileImageBody)
        }
    }
