plugins {
    id("com.qure.feature")
}

android {
    namespace = "com.qure.home"
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
    implementation(project(":presentation:history"))
    implementation(project(":presentation:memo"))
    implementation(project(":presentation:mypage"))

    api(libs.glide.core)
    api(libs.jetpack.activity)
    api(libs.location)
    api(libs.jetpack.fragment)
}
