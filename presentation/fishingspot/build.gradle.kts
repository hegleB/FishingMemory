plugins {
    id("com.qure.feature")
}

android {
    namespace = "com.qure.fishingspot"
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
    implementation(project(":presentation:memo"))
    api(libs.bundles.navermap.compose)
    api(libs.jetpack.activity)
}
