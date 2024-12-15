package com.qure.main

import com.qure.model.extensions.Empty

sealed interface DeepLinkType {
    val uri: String get() = String.Empty

    data class Branch(override val uri: String) : DeepLinkType

    data class Kakao(override val uri: String) : DeepLinkType

    data object None : DeepLinkType

    companion object {
        fun fromUri(uri: String): DeepLinkType {
            return when {
                uri.contains("fishingmemory://open") -> Branch(uri)
                uri.contains("://kakaolink") -> Kakao(uri)
                else -> None
            }
        }
    }
}