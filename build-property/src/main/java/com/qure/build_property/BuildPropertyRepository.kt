package com.qure.build_property

interface BuildPropertyRepository {
    fun get(buildProperty: BuildProperty): String

    fun getOrNull(buildProperty: BuildProperty): String?
}
