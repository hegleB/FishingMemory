plugins {
    id("com.qure.feature")
}

android {
    namespace = "com.qure.gallery"
    buildFeatures {
        dataBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.7"
    }
}

dependencies {
    implementation(project(":build-property"))
    implementation(project(":core-design"))
    implementation(project(":core"))
    implementation(project(":navigator"))
    implementation(project(":domain"))

    api(libs.glide.core)
    kapt(libs.hilt.compiler)
}
