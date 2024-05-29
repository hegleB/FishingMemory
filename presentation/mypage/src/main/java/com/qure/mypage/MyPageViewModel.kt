package com.qure.mypage

import androidx.lifecycle.viewModelScope
import com.qure.core.BaseViewModel
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
import com.qure.domain.usecase.member.LogoutUserUseCase
import com.qure.domain.usecase.member.WithdrawServiceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel
    @Inject
    constructor(
        private val logoutUserUseCase: LogoutUserUseCase,
        private val authRepository: AuthRepository,
        private val withdrawServiceUseCase: WithdrawServiceUseCase,
    ) : BaseViewModel() {
        private val _logoutSucceed: MutableStateFlow<Boolean> = MutableStateFlow(false)
        val logoutSucceed = _logoutSucceed.asStateFlow()

        private val _withdrawSucceed: MutableStateFlow<Boolean> = MutableStateFlow(false)
        val withdrawSucceed = _withdrawSucceed.asStateFlow()

        fun logoutUser() {
            viewModelScope.launch {
                logoutUserUseCase(authRepository.getEmailFromLocal())
                    .catch { throwable -> sendErrorMessage(throwable) }
                    .collect {
                        _logoutSucceed.emit(true)
                    }
            }
        }

        fun withdrawService() {
            viewModelScope.launch {
                withdrawServiceUseCase(getStructuredQuery())
                    .catch { throwable -> sendErrorMessage(throwable) }
                    .collect {
                        _withdrawSucceed.emit(true)
                    }
            }
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

        companion object {
            const val EMAIL = "email"
            const val DATE = "date"
            const val DESCENDING = "DESCENDING"
            const val EQUAL = "EQUAL"
            const val AND = "AND"
            const val COLLECTION_ID = "memo"
        }
    }
