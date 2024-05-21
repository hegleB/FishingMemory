package com.qure.domain.usecase.memo

import com.qure.domain.entity.memo.MemoStorage
import com.qure.domain.repository.MemoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

class UploadMemoImageUseCase
    @Inject
    constructor(
        private val memoRepository: MemoRepository,
    ) {
        operator fun invoke(image: File): Flow<MemoStorage> {
            return flow {
                emit(memoRepository.uploadMemoImage(image))
            }
        }
    }
