plugins {
    id("com.qure.feature")
}

android {
    namespace = "com.qure.permission"
    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(project(":core-design"))
    implementation(project(":core"))
    implementation(project(":navigator"))
    implementation(project(":domain"))
    api(libs.hilt.core)
    kapt(libs.hilt.compiler)
}
