plugins {
    id("com.qure.library")
    id("com.qure.hilt")
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.qure.memo"
    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(project(":core-design"))
    implementation(project(":core"))
    implementation(project(":navigator"))
    implementation(project(":domain"))
    api(platform(libs.firebase.bom))
    api(libs.jetpack.activity)
    api(libs.jetpack.fragment)
    api(libs.firebase.link)
    api(libs.glide.core)
    api(libs.kakao.user)
    api(libs.kakao.talk)
    api(libs.kakao.story)
    api(libs.kakao.share)
    api(libs.hilt.core)
    kapt(libs.hilt.compiler)
    api(libs.hilt.okhttp)
}
