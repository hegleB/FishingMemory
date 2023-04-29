package com.qure.map

import androidx.lifecycle.viewModelScope
import com.naver.maps.map.overlay.Marker
import com.qure.core.BaseViewModel
import com.qure.domain.entity.MarkerType
import com.qure.domain.entity.fishingspot.*
import com.qure.domain.entity.fishingspot.StructuredQuery
import com.qure.domain.entity.fishingspot.Where
import com.qure.domain.entity.memo.*
import com.qure.domain.entity.memo.CollectionId
import com.qure.domain.repository.AuthRepository
import com.qure.domain.usecase.fishingspot.GetFishingSpotUseCase
import com.qure.domain.usecase.memo.GetFilteredMemoUseCase
import com.qure.map.model.toFishingSpotUI
import com.qure.memo.MemoListViewModel
import com.qure.memo.model.toMemoUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getFishingSpotUseCase: GetFishingSpotUseCase,
    private val getFilteredMemoUseCase: GetFilteredMemoUseCase,
    private val authRepository: AuthRepository,
) : BaseViewModel() {

    private val _markers = MutableStateFlow<List<Any>>(emptyList())
    val markers: StateFlow<List<Any>>
        get() = _markers

    fun getFishingSpot(fishingGroundType: MarkerType) {
        viewModelScope.launch {
            getFishingSpotUseCase(getStructuredQuery(fishingGroundType.value)).collect { response ->
                response.onSuccess { result ->
                    _markers.value = result.map { it.toFishingSpotUI() }
                }.onFailure { throwable ->
                    sendErrorMessage(throwable)
                }
            }
        }
    }

    fun getFilteredMemo() {
        viewModelScope.launch {
            getFilteredMemoUseCase(
                getMemoStructuredQuery()
            ).collect { response ->
                response.onSuccess { result ->
                    _markers.value = result.map { it.toMemoUI() }
                }.onFailure { throwable ->
                    sendErrorMessage(throwable)
                }
            }
        }
    }

    private fun getMemoStructuredQuery(): MemoQuery {
        val emailFilter = FieldFilter(
            field = FieldPath(MemoListViewModel.EMAIL),
            op = MemoListViewModel.EQUAL,
            value = Value(authRepository.getEmailFromLocal())
        )

        val compositeFilter = CompositeFilter(
            op = MemoListViewModel.AND,
            filters = listOf(Filter(emailFilter))
        )

        return MemoQuery(
            com.qure.domain.entity.memo.StructuredQuery(
                from = listOf(CollectionId(MemoListViewModel.COLLECTION_ID)),
                where = com.qure.domain.entity.memo.Where(compositeFilter),
                orderBy = listOf(
                    OrderBy(
                        FieldPath(MemoListViewModel.DATE),
                        MemoListViewModel.DESCENDING
                    )
                )
            )
        )
    }


    private fun getStructuredQuery(fishingGroundType: String): FishingSpotQuery {
        val fieldFilter = FieldFilter(
            op = "EQUAL",
            field = FieldPath("fishing_ground_type"),
            value = Value(fishingGroundType),
        )

        return FishingSpotQuery(
            StructuredQuery(
                from = listOf(CollectionId(FISHING_SPOT_COLLECTION_ID)),
                where = Where(fieldFilter),
            )
        )

    }

    companion object {
        const val FISHING_SPOT_COLLECTION_ID = "fishingspot"
    }
}

