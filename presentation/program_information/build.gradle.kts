plugins {
    id("com.qure.library")
    id("com.qure.hilt")
}

android {
    namespace = "com.qure.program_information"
    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(project(":core-design"))
    implementation(project(":core"))
    implementation(project(":navigator"))
    implementation(project(":domain"))
    kapt(libs.hilt.compiler)
}
