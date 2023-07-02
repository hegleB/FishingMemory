package com.qure.create

import androidx.lifecycle.viewModelScope
import com.qure.build_property.BuildProperty
import com.qure.build_property.BuildPropertyRepository
import com.qure.core.BaseViewModel
import com.qure.core.extensions.Empty
import com.qure.core.extensions.Slash
import com.qure.core.extensions.URLSplash
import com.qure.core.extensions.UUID
import com.qure.domain.entity.memo.FieldStringValue
import com.qure.domain.entity.memo.MemoFields
import com.qure.domain.entity.memo.MemoStorage
import com.qure.domain.repository.AuthRepository
import com.qure.domain.usecase.memo.CreateMemoUseCase
import com.qure.domain.usecase.memo.UpdateMemoUseCase
import com.qure.domain.usecase.memo.UploadMemoImageUseCase
import com.qure.memo.model.MemoUI
import com.qure.memo.model.toMemoUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class MemoViewModel @Inject constructor(
    private val createMemoUseCase: CreateMemoUseCase,
    private val uploadMemoImageUseCase: UploadMemoImageUseCase,
    private val updateMemoUseCase: UpdateMemoUseCase,
    private val authRepository: AuthRepository,
    private val buildPropertyRepository: BuildPropertyRepository,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _memo = MutableStateFlow(MemoUI())
    val memo = _memo.asStateFlow()
    fun createMemo(imageUrl: String) {
        val memo = createMemoFields(imageUrl)
        viewModelScope.launch {
            createMemoUseCase(memo).onSuccess {
                _uiState.update {
                    it.copy(
                        isSave = true
                    )
                }
            }.onFailure { throwable ->
                throwable as Exception
                sendErrorMessage(throwable)
            }
        }
    }

    fun setMemoUI(memoUI: MemoUI) {
        _memo.value = memoUI
    }

    private fun createMemoFields(imageUrl: String, uuid: String = String.UUID): MemoFields {
        return MemoFields(
            uuid = FieldStringValue(uuid),
            email = FieldStringValue(authRepository.getEmailFromLocal()),
            title = FieldStringValue(memo.value.title),
            image = FieldStringValue(imageUrl),
            waterType = FieldStringValue(memo.value.waterType),
            fishType = FieldStringValue(memo.value.fishType),
            location = FieldStringValue(memo.value.location),
            date = FieldStringValue(memo.value.date),
            fishSize = FieldStringValue(memo.value.fishSize),
            content = FieldStringValue(memo.value.content),
            coords = FieldStringValue(memo.value.coords)
        )
    }

    fun uploadMemoImage(uuid: String = String.Empty) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isUploadImage = true
                )
            }
            uploadMemoImageUseCase(File(uiState.value.image))
                .onSuccess { storage ->
                    if (uuid == String.Empty) {
                        createMemo(getImageUrl(storage))
                    } else {
                        updateMemo(uuid, getImageUrl(storage))
                    }
                }.onFailure { throwable ->
                    throwable is Exception
                    sendErrorMessage(throwable)
                    _uiState.update {
                        it.copy(
                            isUploadImage = false
                        )
                    }

                }
        }
    }

    fun updateMemo(uuid: String, imageUrl: String) {
        val editedMemo = createMemoFields(imageUrl, uuid)
        viewModelScope.launch {
            updateMemoUseCase(editedMemo).collect { response ->
                response.onSuccess { memo ->
                    _uiState.update {
                        it.copy(
                            isUpdated = true,
                            memo = memo.toMemoUI()
                        )
                    }
                }
            }
        }
    }

    private fun getImageUrl(storage: MemoStorage): String {
        val fileName = storage.name.split(String.Slash).joinToString(String.URLSplash)
        return buildPropertyRepository.get(BuildProperty.FIREBASE_STORAGE_URL) +
                "o/${fileName}?alt=media&token=${storage.downloadTokens}"
    }

    fun setImage(image: String) {
        _uiState.update {
            it.copy(
                image = image
            )
        }
    }
}

data class UiState(
    val isSave: Boolean = false,
    val isUploadImage: Boolean = false,
    val isUpdated: Boolean = false,
    val memo: MemoUI? = null,
    val image: String = String.Empty,
)