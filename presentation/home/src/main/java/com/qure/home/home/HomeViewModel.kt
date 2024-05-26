package com.qure.home.home

import androidx.lifecycle.viewModelScope
import com.qure.core.BaseViewModel
import com.qure.core.extensions.DefaultLatitude
import com.qure.core.extensions.DefaultLongitude
import com.qure.core.extensions.twoDigitsFormat
import com.qure.domain.entity.memo.CollectionId
import com.qure.domain.entity.memo.CompositeFilter
import com.qure.domain.entity.memo.FieldFilter
import com.qure.domain.entity.memo.FieldPath
import com.qure.domain.entity.memo.Filter
import com.qure.domain.entity.memo.MemoQuery
import com.qure.domain.entity.memo.OrderBy
import com.qure.domain.entity.memo.StructuredQuery
import com.qure.domain.entity.memo.Value
import com.qure.domain.entity.memo.Where
import com.qure.domain.repository.AuthRepository
import com.qure.domain.usecase.memo.GetFilteredMemoUseCase
import com.qure.domain.usecase.weather.GetWeatherUseCase
import com.qure.home.home.model.toWeatherUI
import com.qure.memo.model.toMemoUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val getFilteredMemoUseCase: GetFilteredMemoUseCase,
    private val authRepository: AuthRepository,
) : BaseViewModel() {

    private val _selectedChip = MutableStateFlow(INITIAL_FISH_TYPE)
    val selectedChip = _selectedChip.asStateFlow()
    private val _latLng = MutableStateFlow(
        LatXLngY(
            String.DefaultLatitude.toDouble(),
            String.DefaultLongitude.toDouble(),
        )
    )
    val latLng = _latLng.asStateFlow()

    private val _homeUiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val homeUiState = _homeUiState.asStateFlow()

    init {
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {
            combine(
                getWeatherUseCase(
                    base_date = getBaseDate(),
                    base_time = getBaseTime(),
                    nx = _latLng.value.nx.toInt().toString(),
                    ny = _latLng.value.ny.toInt().toString(),
                ),
                getFilteredMemoUseCase(
                    getStructuredQuery(),
                ),
                ::Pair
            )
                .map { ui ->
                    HomeUiState.Success(
                        weather = ui.first.response.body.items.item.map { it.toWeatherUI() },
                        memos = ui.second.map { it.toMemoUI() }
                    )
                }
                .onStart { _homeUiState.value = HomeUiState.Loading }
                .catch { throwable -> sendErrorMessage(throwable) }
                .collectLatest { homeUiState ->
                    _homeUiState.value = homeUiState
                }
        }
    }


    fun setSelectedChip(chip: String) {
        _selectedChip.value = chip
    }

    fun setLatLng(latXLngY: LatXLngY) {
        _latLng.value = latXLngY
    }

    private fun getStructuredQuery(): MemoQuery {
        val emailFilter =
            FieldFilter(
                field = FieldPath(EMAIL),
                op = EQUAL,
                value = Value(authRepository.getEmailFromLocal()),
            )

        val compositeFilter =
            CompositeFilter(
                op = AND,
                filters = listOf(Filter(emailFilter)),
            )

        return MemoQuery(
            StructuredQuery(
                from = listOf(CollectionId(COLLECTION_ID)),
                where = Where(compositeFilter),
                orderBy = listOf(OrderBy(FieldPath(DATE), DESCENDING)),
            ),
        )
    }
    private fun getBaseTime(): String {
        val baseTime = "${(LocalTime.now().hour - 1).twoDigitsFormat()}30"
        return baseTime
    }

    private fun getBaseDate(): Int {
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val baseDate = LocalDate.now().format(formatter).toInt()
        return baseDate
    }

    companion object {
        private const val EMAIL = "email"
        private const val DATE = "date"
        private const val DESCENDING = "DESCENDING"
        private const val EQUAL = "EQUAL"
        private const val AND = "AND"
        private const val COLLECTION_ID = "memo"
        private const val INITIAL_FISH_TYPE = "어종"
    }
}