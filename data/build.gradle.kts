plugins {
    id("com.qure.library")
    id("com.qure.hilt")
}

android {
    namespace = "com.qure.data"
}

dependencies {
    implementation(project(":build-property"))
    implementation(project(":domain"))
    implementation(project(":navigator"))

    api(libs.gson)
    api(libs.room.runtime)
    api(libs.room.ktx)
    kapt(libs.room.complier)
    api(libs.network.core)
    api(libs.network.adapter)
    api(libs.network.interceptor)
    api(libs.jetpack.datastore.preferences.core)
    api(libs.jetpack.datastore.preferences)
    api(libs.jetpack.ktx)
    api(libs.hilt.core)
    kapt(libs.hilt.compiler)
    api(libs.hilt.okhttp)
}