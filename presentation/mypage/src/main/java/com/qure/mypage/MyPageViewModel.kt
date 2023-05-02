package com.qure.mypage

import androidx.lifecycle.viewModelScope
import com.qure.core.BaseViewModel
import com.qure.domain.entity.memo.*
import com.qure.domain.repository.AuthRepository
import com.qure.domain.usecase.member.LogoutUserUseCase
import com.qure.domain.usecase.member.WithdrawServiceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val logoutUserUseCase: LogoutUserUseCase,
    private val authRepository: AuthRepository,
    private val withdrawServiceUseCase: WithdrawServiceUseCase,
) : BaseViewModel() {

    private val _logoutSucceed: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val logutSucceed: MutableStateFlow<Boolean>
        get() = _logoutSucceed

    private val _withdrawSucceed: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val withdrawSucceed: MutableStateFlow<Boolean>
        get() = _withdrawSucceed

    fun logoutUser() {
        viewModelScope.launch {
            logoutUserUseCase(authRepository.getEmailFromLocal()).collect { response ->
                response.onSuccess {
                    _logoutSucceed.emit(true)
                }.onFailure { thorwable ->
                    sendErrorMessage(thorwable.message ?: "로그아웃에 실패했습니다. 잠시 후 다시 시도해주세요.")
                }
            }
        }
    }

    fun withdrawService() {
        viewModelScope.launch {
            withdrawServiceUseCase(getStructuredQuery()).collect { response ->
                response.onSuccess {
                    _withdrawSucceed.emit(it)
                }.onFailure { throwable ->
                    sendErrorMessage(throwable.message ?: "회원탈퇴를 실패했습니다. 잠시 후 다시 시도해주세요.")
                }
            }
        }
    }

    private fun getStructuredQuery(): MemoQuery {
        val emailFilter = FieldFilter(
            field = FieldPath(EMAIL),
            op = EQUAL,
            value = Value(authRepository.getEmailFromLocal())
        )

        val compositeFilter = CompositeFilter(
            op = AND,
            filters = listOf(Filter(emailFilter))
        )

        return MemoQuery(
            StructuredQuery(
                from = listOf(CollectionId(COLLECTION_ID)),
                where = Where(compositeFilter),
                orderBy = listOf(OrderBy(FieldPath(DATE), DESCENDING))
            )
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