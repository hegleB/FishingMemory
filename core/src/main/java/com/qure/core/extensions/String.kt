package com.qure.core.extensions

import java.util.*

val String.Companion.Empty
    get() = ""

val String.Companion.Spacing
    get() = " "

val String.Companion.UUID
    get() = generateUniqueId()

val String.Companion.Slash
    get() = "/"

val String.Companion.Dash
    get() = "-"

fun generateUniqueId(): String {
    return UUID.randomUUID().toString().replace("-","")
}