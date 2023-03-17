package com.qure.data.entity.base

data class ErrorResponse(
    val error: Error,
)

data class Error(
    val message: String,
    val code: String,
    val errors: List<Errors>,
)

data class Errors(
    val message: String,
    val domain: String,
    val reason: String,
)