plugins {
    id("com.qure.feature")
}

android {
    namespace = "com.qure.program_information"
    buildFeatures {
        dataBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.7"
    }
}

dependencies {
    implementation(project(":core-design"))
    implementation(project(":core"))
    implementation(project(":navigator"))
    implementation(project(":domain"))
    kapt(libs.hilt.compiler)
}
