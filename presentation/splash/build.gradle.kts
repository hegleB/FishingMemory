plugins {
    id("com.qure.feature")
}

android {
    namespace = "com.qure.splash"
    buildFeatures {
        dataBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.7"
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":navigator"))
    implementation(project(":domain"))
    implementation(project(":presentation:onboarding"))
    implementation(project(":testing"))

    api(libs.jetpack.constraintLayout)
}
