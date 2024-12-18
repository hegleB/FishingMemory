package com.qure.create

import androidx.lifecycle.viewModelScope
import com.qure.build_property.BuildProperty
import com.qure.build_property.BuildPropertyRepository
import com.qure.data.repository.auth.AuthRepository
import com.qure.data.utils.NetworkMonitor
import com.qure.domain.usecase.memo.CreateMemoUseCase
import com.qure.domain.usecase.memo.UpdateMemoUseCase
import com.qure.domain.usecase.memo.UploadMemoImageUseCase
import com.qure.model.extensions.Slash
import com.qure.model.extensions.URLSplash
import com.qure.model.extensions.UUID
import com.qure.model.memo.Document
import com.qure.model.memo.MemoStorage
import com.qure.ui.base.BaseViewModel
import com.qure.ui.model.MemoUI
import com.qure.ui.model.SnackBarMessageType
import com.qure.ui.model.toMemoFields
import com.qure.ui.model.toMemoUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MemoViewModel
@Inject
constructor(
    private val createMemoUseCase: CreateMemoUseCase,
    private val uploadMemoImageUseCase: UploadMemoImageUseCase,
    private val updateMemoUseCase: UpdateMemoUseCase,
    private val authRepository: AuthRepository,
    private val buildPropertyRepository: BuildPropertyRepository,
    private val networkMonitor: NetworkMonitor,
) : BaseViewModel() {
    private val _memoCreateUiState = MutableStateFlow<MemoCreateUiState>(MemoCreateUiState.Initial)
    val memoCreateUiState = _memoCreateUiState.asStateFlow()

    private val _memo = MutableStateFlow(
        MemoUI(
            email = authRepository.getEmailFromLocal(),
            date = SimpleDateFormat("yyyy/MM/dd", Locale.KOREA).format(Date()),
        )
    )
    val memo = _memo.asStateFlow()

    private val _isEdit = MutableStateFlow(false)
    val isEdit = _isEdit.asStateFlow()

    val isConnectNetwork = networkMonitor.isConnectNetwork
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = true,
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    fun createMemo() {
        viewModelScope.launch(Dispatchers.IO) {
            uploadImageIfNeeded()
                .onStart { _memoCreateUiState.value = MemoCreateUiState.Loading }
                .catch { throwable -> sendErrorMessage(throwable) }
                .flatMapConcat { imageUrl ->
                    _memo.update {
                        it.copy(
                            uuid = String.UUID,
                            image = imageUrl,
                        )
                    }
                    createMemoUseCase(
                        memo.value.toMemoFields(authRepository.getEmailFromLocal())
                    )
                        .onStart {
                            _memoCreateUiState.value = MemoCreateUiState.Loading
                        }
                        .catch { throwable -> sendErrorMessage(throwable) }
                }
                .collectLatest {
                    _memoCreateUiState.value = MemoCreateUiState.Success(memo = memo.value)
                    sendMessage(SnackBarMessageType.SAVE_MEMO)
                }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun uploadImageIfNeeded(): Flow<String> {
        return if (_memo.value.image.startsWith("https://firebasestorage").not()) {
            uploadMemoImageUseCase(File(_memo.value.image))
                .flowOn(Dispatchers.IO)
                .flatMapConcat { storage ->
                    val imageUrl = getImageUrl(storage)
                    _memo.update { it.copy(image = imageUrl) }
                    flowOf(imageUrl)
                }
        } else {
            flowOf(_memo.value.image)
        }
    }

    private fun updateMemoFlow(): Flow<Document> {
        return updateMemoUseCase(_memo.value.toMemoFields(authRepository.getEmailFromLocal()))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun updateMemo() {
        viewModelScope.launch {
            uploadImageIfNeeded()
                .onStart { _memoCreateUiState.value = MemoCreateUiState.Loading }
                .catch { throwable ->
                    sendErrorMessage(throwable)
                }
                .flatMapConcat {
                    updateMemoFlow()
                }
                .collectLatest { document ->
                    _memoCreateUiState.value = MemoCreateUiState.Success(document.toMemoUI())
                    sendMessage(SnackBarMessageType.UPDATE_MEMO)
                }
        }
    }

    fun setMemoUi(memoUI: MemoUI) {
        _memo.update { memoUI.copy(date = _memo.value.date, email = _memo.value.email) }
    }

    fun setEditMode(isEdit: Boolean) {
        _isEdit.value = isEdit
    }

    private fun getImageUrl(storage: MemoStorage): String {
        val fileName = storage.name.split(String.Slash).joinToString(String.URLSplash)
        return buildPropertyRepository.get(BuildProperty.FIREBASE_STORAGE_URL) +
                "o/$fileName?alt=media&token=${storage.downloadTokens}"
    }

    fun setImage(image: String) {
        _memo.update {
            it.copy(image = image)
        }
    }

    fun setCoords(coords: String) {
        _memo.update {
            it.copy(coords = coords)
        }

    }

    fun setLocation(location: String) {
        _memo.update {
            it.copy(location = location)
        }

    }

    fun setDate(date: String) {
        _memo.update {
            it.copy(date = date)
        }

    }

    fun setTitle(title: String) {
        _memo.update {
            it.copy(title = title)
        }
    }

    fun setFishType(fishType: String) {
        _memo.update {
            it.copy(fishType = fishType)
        }
    }

    fun setContent(content: String) {
        _memo.update {
            it.copy(content = content)
        }
    }

    fun setSize(size: String) {
        _memo.update {
            it.copy(fishSize = size)
        }
    }

    fun setWaterType(waterType: String) {
        _memo.update {
            it.copy(waterType = waterType)
        }
    }
}