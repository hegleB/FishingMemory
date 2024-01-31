plugins {
    id("com.qure.feature")
}

android {
    namespace = "com.qure.create"
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
    implementation(project(":presentation:memo"))

    api(libs.jetpack.fragment)
    api(libs.glide.core)
    api(libs.navermap.sdk)
}
