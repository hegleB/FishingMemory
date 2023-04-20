package com.qure.create

import androidx.lifecycle.viewModelScope
import com.qure.build_property.BuildProperty
import com.qure.build_property.BuildPropertyRepository
import com.qure.core.BaseViewModel
import com.qure.core.extensions.Empty
import com.qure.core.extensions.UUID
import com.qure.domain.entity.memo.FieldValue
import com.qure.domain.entity.memo.MemoFields
import com.qure.domain.entity.memo.MemoStorage
import com.qure.domain.repository.AuthRepository
import com.qure.domain.usecase.memo.CreateMemoUseCase
import com.qure.domain.usecase.memo.UploadMemoImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MemoViewModel @Inject constructor(
    private val createMemoUseCase: CreateMemoUseCase,
    private val uploadMemoImageUseCase: UploadMemoImageUseCase,
    private val authRepository: AuthRepository,
    private val buildPropertyRepository: BuildPropertyRepository,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState>
        get() = _uiState

    private val _title = MutableStateFlow(String.Empty)
    val title: StateFlow<String>
        get() = _title

    private val _image = MutableStateFlow(File("/path/to/file"))
    val image: StateFlow<File>
        get() = _image

    private val _waterType = MutableStateFlow(String.Empty)
    val waterType: StateFlow<String>
        get() = _waterType

    private val _fishType = MutableStateFlow(String.Empty)
    val fishType: StateFlow<String>
        get() = _fishType

    private val _fishSize = MutableStateFlow(String.Empty)
    val fishSize: StateFlow<String>
        get() = _fishSize

    private val _location = MutableStateFlow(String.Empty)
    val location: StateFlow<String>
        get() = _location

    private val _date = MutableStateFlow(String.Empty)
    val date: StateFlow<String>
        get() = _date

    private val _content = MutableStateFlow(String.Empty)
    val content: StateFlow<String>
        get() = _content

    fun createMemo(imageUrl: String) {
        val memo = MemoFields(
            uuid = FieldValue(String.UUID),
            email = FieldValue(authRepository.getEmailFromLocal()),
            title = FieldValue(title.value),
            image = FieldValue(imageUrl),
            waterType = FieldValue(waterType.value),
            fishType = FieldValue(fishType.value),
            location = FieldValue(location.value),
            date = FieldValue(date.value),
            fishSize = FieldValue(fishSize.value),
            content = FieldValue(content.value)
        )
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

    fun uploadMemoImage() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isUploadImage = true
                )
            }
            uploadMemoImageUseCase(image.value)
                .onSuccess { storage ->
                    createMemo(getImageUrl(storage))
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

    private fun getImageUrl(storage: MemoStorage): String {
        val fileName = storage.name.split("/").joinToString("%2F")
        return buildPropertyRepository.get(BuildProperty.FIREBASE_STORAGE_URL) +
                "o/${fileName}?alt=media&token=${storage.downloadTokens}"
    }

    fun setImage(image: File) {
        _image.value = image
    }

    fun setTitle(title: String) {
        _title.value = title
    }

    fun setWaterType(watherType: String) {
        _waterType.value = watherType
    }

    fun setFishType(fishType: String) {
        _fishType.value = fishType
    }

    fun setFishSize(fishSize: String) {
        _fishSize.value = fishSize
    }

    fun setLocation(location: String) {
        _location.value = location
    }

    fun setDate(date: String) {
        _date.value = date
    }

    fun setContent(content: String) {
        _content.value = content
    }
}

data class UiState(
    val isSave: Boolean = false,
    val isUploadImage: Boolean = false,
)