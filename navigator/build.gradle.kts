plugins {
    id("com.qure.library")
    id("com.qure.hilt")
}

android {
    namespace = "com.qure.navigator"
}

dependencies {
    api(libs.hilt.core)
    kapt(libs.hilt.compiler)
    api(libs.hilt.okhttp)
}