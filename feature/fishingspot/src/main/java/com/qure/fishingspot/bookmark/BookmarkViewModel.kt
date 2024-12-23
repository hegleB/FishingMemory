package com.qure.fishingspot.bookmark

import androidx.lifecycle.viewModelScope
import com.qure.domain.usecase.bookmark.DeleteAllFishingSpotBookmarkUseCase
import com.qure.domain.usecase.bookmark.GetFishingSpotBookmarksUseCase
import com.qure.fishingspot.FishingSpotUiState
import com.qure.model.toFishingSpotUI
import com.qure.ui.base.BaseViewModel
import com.qure.ui.model.SnackBarMessageType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel
@Inject
constructor(
    private val getFishingSpotBookmarksUseCase: GetFishingSpotBookmarksUseCase,
    private val deleteAllFishingSpotBookmarkUseCase: DeleteAllFishingSpotBookmarkUseCase,
) : BaseViewModel() {

    private val _fishingSpotUiState = MutableStateFlow<FishingSpotUiState>(FishingSpotUiState.Loading)
    val fishingSpotUiState = _fishingSpotUiState.asStateFlow()

    init {
        fetchFishingSpotBookmark()
    }

    fun fetchFishingSpotBookmark() {
        viewModelScope.launch {
            getFishingSpotBookmarksUseCase()
                .map { fishingSpots -> FishingSpotUiState.Success(fishingSpots.map { it.toFishingSpotUI() }.toImmutableList()) }
                .catch { throwable -> sendErrorMessage(throwable) }
                .collectLatest { fishingSpotUiState ->
                    _fishingSpotUiState.value = fishingSpotUiState
                }
        }
    }

    fun deleteAllBookmarks() {
        viewModelScope.launch {
            flow {  emit(deleteAllFishingSpotBookmarkUseCase()) }
                .catch { throwable -> sendErrorMessage(throwable) }
                .collectLatest {
                    sendMessage(SnackBarMessageType.DELETE_ALL_BOOKMARK)
                }
            fetchFishingSpotBookmark()
        }
    }
}
