package com.qure.core.extensions

import java.util.*

val String.Companion.Empty
    get() = ""

val String.Companion.Spacing
    get() = " "

val String.Companion.UUID
    get() = generateUniqueId()

fun generateUniqueId(): String {
    return UUID.randomUUID().toString().replace("-","")
}