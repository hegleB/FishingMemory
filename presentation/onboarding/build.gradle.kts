plugins {
    id("com.qure.feature")
}

android {
    namespace = "com.qure.onboarding"
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
    kapt(libs.hilt.compiler)
}
