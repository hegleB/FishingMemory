package com.qure.fishingspot

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.qure.core.BaseViewModel
import com.qure.domain.entity.fishingspot.FishingSpotBookmark
import com.qure.domain.usecase.bookmark.CheckFishingSpotBookmarkUseCase
import com.qure.domain.usecase.bookmark.DeleteFishingSpotBookmarkUseCase
import com.qure.domain.usecase.bookmark.InsertFishingSpotBookmarkUseCase
import com.qure.model.FishingSpotUI
import com.qure.model.toFishingSpotBookmark
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FishingSpotViewModel
    @Inject
    constructor(
        private val deleteFishingSpotBookmarkUseCase: DeleteFishingSpotBookmarkUseCase,
        private val insertFishingSpotBookmarkUseCase: InsertFishingSpotBookmarkUseCase,
        private val checkFishingSpotBookmarkUseCase: CheckFishingSpotBookmarkUseCase,
    ) : BaseViewModel() {
        var isBookmarkClicked by mutableStateOf(false)

        fun deleteBookmark(fishingSpotBookmark: FishingSpotBookmark) {
            viewModelScope.launch {
                deleteFishingSpotBookmarkUseCase(fishingSpotBookmark)
                isBookmarkClicked = false
            }
        }

        fun toggleBookmarkButton(fishingSpotUI: FishingSpotUI) {
            viewModelScope.launch {
                if (isBookmarkClicked) {
                    deleteBookmark(fishingSpotUI.toFishingSpotBookmark())
                } else {
                    insertBookmark(fishingSpotUI.toFishingSpotBookmark())
                }
            }
        }

        fun checkBookmark(number: Int) {
            viewModelScope.launch {
                checkFishingSpotBookmarkUseCase(number)
                    .catch { throwable -> sendErrorMessage(throwable) }
                    .collectLatest { isBookmark ->
                        isBookmarkClicked = isBookmark
                    }
            }
        }

        fun insertBookmark(fishingSpotBookmark: FishingSpotBookmark) {
            viewModelScope.launch {
                insertFishingSpotBookmarkUseCase(fishingSpotBookmark)
                isBookmarkClicked = true
            }
        }
    }
