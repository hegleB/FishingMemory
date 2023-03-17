package com.qure.data.entity.base

data class ApiResponse<T>(
    val data: T?,
    val message: String,
    val code: Int,
)

fun <T> Result<ApiResponse<T>>.getResult(): Result<T>? {
    this.onSuccess { response ->
        val data = response.data ?: return null
        return Result.success(data)
    }.onFailure { throwable ->
        return Result.failure(throwable)
    }
    return Result.failure(IllegalStateException("알 수 없는 오류가 발생하였습니다."))
}
