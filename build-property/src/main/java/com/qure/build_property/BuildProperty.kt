package com.qure.build_property

enum class BuildProperty(
    private val description: String
) {
    KAKAO_API_KEY("kakao native app key"),
    FIREBASE_DATABASE_PROJECT_ID("firebase 프로젝트 ID"),
    FIREBASE_DATABASE_URL("firebase 데이터베이스 주소"),
    FIREBASE_STORAGE_URL("firebase storage 주소"),
    FIREBASE_API_KEY("firebase api key"),
    WEATHER_DATABASE_URL("weather 데이터베이스 주소"),
    WEATHER_API_KEY("weather api key"),
    NAVER_MAP_BASE_URL("naver map base 주소"),
    NAVER_MAP_API_CLIENT_ID("naver map client id"),
    NAVER_MAP_API_CLIENT_SECRET("naver_map_api_client_secret");

    val key: String = this.name

    override fun toString(): String {
        return "BuildProperty(description='$description', key='$key')"
    }
}