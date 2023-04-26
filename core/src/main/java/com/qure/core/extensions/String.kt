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

val String.Companion.URLSplash
    get() = "%2F"

val String.Companion.Colon
    get() = ":"

val String.Companion.HashTag
    get() = " #"

val String.Companion.Comma
    get() = ","

val String.Companion.DefaultLatitude
    get() = "37.5666102"

val String.Companion.DefaultLongitude
    get() = "126.9783881"
fun generateUniqueId(): String {
    return UUID.randomUUID().toString().replace(String.Dash,String.Empty)
}