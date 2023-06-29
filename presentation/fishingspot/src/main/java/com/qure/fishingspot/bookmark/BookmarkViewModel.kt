package com.qure.fishingspot.bookmark

import androidx.lifecycle.viewModelScope
import com.qure.core.BaseViewModel
import com.qure.domain.usecase.bookmark.DeleteAllFishingSpotBookmarkUseCase
import com.qure.domain.usecase.bookmark.GetFishingSpotBookmarksUseCase
import com.qure.domain.util.Result
import com.qure.model.FishingSpotUI
import com.qure.model.toFishingSpotUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val getFishingSpotBookmarksUseCase: GetFishingSpotBookmarksUseCase,
    private val deleteAllFishingSpotBookmarkUseCase: DeleteAllFishingSpotBookmarkUseCase,
) : BaseViewModel() {

    private val _fishingSpotBookmarks: MutableStateFlow<List<FishingSpotUI>> =
        MutableStateFlow(emptyList())
    val fishingSpotBookmarks: StateFlow<List<FishingSpotUI>>
        get() = _fishingSpotBookmarks
    fun getBookmarks() {
        viewModelScope.launch(Dispatchers.IO) {
            getFishingSpotBookmarksUseCase().collect { response ->
                when (response) {
                    is Result.Loading -> {
                        startLoading()
                    }
                    is Result.Error -> {
                        stopLoading()
                        sendErrorMessage(response.exception)
                    }
                    is Result.Empty -> {
                        stopLoading()
                        _fishingSpotBookmarks.value = emptyList()
                    }
                    is Result.Success -> {
                        _fishingSpotBookmarks.value = response.data.map { it.toFishingSpotUI() }
                        stopLoading()
                    }
                }
            }
        }
    }
    fun deleteAllBookmarks() {
        viewModelScope.launch {
            deleteAllFishingSpotBookmarkUseCase()
            _fishingSpotBookmarks.value = emptyList()
        }
    }
}