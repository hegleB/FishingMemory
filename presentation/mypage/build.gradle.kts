plugins {
    id("com.qure.feature")
}

android {
    namespace = "com.qure.mypage"
    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(project(":core-design"))
    implementation(project(":core"))
    implementation(project(":navigator"))
    implementation(project(":domain"))

    api(libs.jetpack.activity)
    api(libs.jetpack.fragment)
    api(libs.kakao.user)
    kapt(libs.hilt.compiler)
    api(libs.hilt.core)
    api(libs.hilt.okhttp)
}
