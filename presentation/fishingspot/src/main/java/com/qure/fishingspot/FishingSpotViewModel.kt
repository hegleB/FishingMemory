package com.qure.fishingspot

import androidx.lifecycle.viewModelScope
import com.qure.core.BaseViewModel
import com.qure.domain.entity.fishingspot.FishingSpotBookmark
import com.qure.domain.usecase.bookmark.*
import com.qure.domain.util.Result
import com.qure.model.FishingSpotUI
import com.qure.model.toFishingSpotBookmark
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
        private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
        val uiState = _uiState.asStateFlow()

        fun deleteBookmark(fishingSpotBookmark: FishingSpotBookmark) {
            viewModelScope.launch {
                deleteFishingSpotBookmarkUseCase(fishingSpotBookmark)
                _uiState.update {
                    it.copy(
                        isBookmarkClicked = false,
                    )
                }
            }
        }

        fun toggleBookmarkButton(fishingSpotUI: FishingSpotUI) {
            viewModelScope.launch {
                if (uiState.value.isBookmarkClicked) {
                    deleteBookmark(fishingSpotUI.toFishingSpotBookmark())
                } else {
                    insertBookmark(fishingSpotUI.toFishingSpotBookmark())
                }
            }
        }

        fun checkBookmark(number: Int) {
            viewModelScope.launch(Dispatchers.IO) {
                checkFishingSpotBookmarkUseCase(number).collect { response ->
                    when (response) {
                        is Result.Loading -> startLoading()
                        is Result.Success -> {
                            _uiState.update {
                                it.copy(
                                    isBookmarkClicked = response.data,
                                    isBookmarked = response.data,
                                )
                            }
                            stopLoading()
                        }

                        is Result.Error -> {
                            sendErrorMessage(response.exception.message)
                            stopLoading()
                        }

                        is Result.Empty -> {
                            stopLoading()
                        }
                    }
                }
            }
        }

        fun insertBookmark(fishingSpotBookmark: FishingSpotBookmark) {
            viewModelScope.launch {
                insertFishingSpotBookmarkUseCase(fishingSpotBookmark)
                _uiState.update {
                    it.copy(
                        isBookmarkClicked = true,
                    )
                }
            }
        }
    }

data class UiState(
    val isBookmarkClicked: Boolean = false,
    val isBookmarked: Boolean = false,
)
