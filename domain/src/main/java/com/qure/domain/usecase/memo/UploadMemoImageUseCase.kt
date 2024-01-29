package com.qure.domain.usecase.memo

import com.qure.domain.entity.memo.MemoStorage
import com.qure.domain.repository.MemoRepository
import java.io.File
import javax.inject.Inject

class UploadMemoImageUseCase
    @Inject
    constructor(
        private val memoRepository: MemoRepository,
    ) {
        suspend operator fun invoke(image: File): Result<MemoStorage> {
            return memoRepository.uploadMemoImage(image)
                .onSuccess {
                }
                .onFailure {
                    throw it as Exception
                }
        }
    }
