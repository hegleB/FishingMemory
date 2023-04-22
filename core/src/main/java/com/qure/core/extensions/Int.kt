package com.qure.core.extensions

fun Int.twoDigitsFormat(): String {
    return "%02d".format(this)
}