plugins {
    id("com.qure.library")
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.qure.core_design"
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.7"
    }
}

dependencies {
    api(libs.chart)
    api(libs.jetpack.ktx)
    api(libs.jetpack.recyclerView)
    api(libs.jetpack.material)
    kapt(libs.hilt.compiler)
    api(platform(libs.compose.bom))
    api(libs.bundles.androidx.compose)
    debugImplementation(libs.compose.ui.tolling)
}
