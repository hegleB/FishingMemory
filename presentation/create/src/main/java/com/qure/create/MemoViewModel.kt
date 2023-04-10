package com.qure.create

import androidx.lifecycle.viewModelScope
import com.qure.core.BaseViewModel
import com.qure.core.extensions.Empty
import com.qure.domain.entity.auth.Email
import com.qure.domain.entity.memo.*
import com.qure.domain.repository.AuthRepository
import com.qure.domain.usecase.auth.GetUserTokenUseCase
import com.qure.domain.usecase.memo.CreateMemoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemoViewModel @Inject constructor(
    private val createMemoUseCase: CreateMemoUseCase,
    private val authRepository: AuthRepository,
) : BaseViewModel() {

    private val _title = MutableStateFlow(String.Empty)
    val title: StateFlow<String>
        get() = _title

    private val _image = MutableStateFlow(String.Empty)
    val image: StateFlow<String>
        get() = _image

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

    fun createMemo() {
        val memo = MemoFields(
            email = Email(authRepository.getEmailFromLocal()),
            title = MemoTitle(title.value),
            image = MemoImage(image.value),
            fishType = FishType(fishType.value),
            location = MemoLocation(location.value),
            date = MemoDate(date.value),
            fishSize = FishSize(fishSize.value),
            content = MemoContent(content.value)
        )
        viewModelScope.launch {
            createMemoUseCase(memo).onSuccess {

            }.onFailure { throwable ->
                throwable as Exception
                sendErrorMessage(throwable)
            }
        }
    }

    fun setTitle(title: String) {
        _title.value = title
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