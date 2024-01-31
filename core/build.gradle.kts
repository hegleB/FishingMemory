plugins {
    id("com.qure.library")
    id("com.qure.hilt")
}

android {
    namespace = "com.qure.core"
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
    implementation(project(":build-property"))

    api(libs.jetpack.appCompat)
    api(libs.jetpack.ktx)
    api(libs.jetpack.lifecycle.viewModel)
    api(libs.jetpack.lifecycle.runtime)
    api(libs.navermap.sdk)
    api(libs.navermap.clustering)
    api(libs.swiprefesh)
    api(libs.hilt.core)
    kapt(libs.hilt.compiler)
    api(libs.hilt.okhttp)
}
