package com.qure.build_property

enum class BuildProperty(
    private val description: String
) {
    KAKAO_API_KEY("kakao native app key"),
    FIREBASE_AUTH_URL("firebase 인증 주소"),
    FIREBASE_API_KEY("firebase api key");

    val key: String = this.name

    override fun toString(): String {
        return "BuildProperty(description='$description', key='$key')"
    }
}