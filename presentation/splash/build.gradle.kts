plugins {
    id("com.qure.feature")
}

android {
    namespace = "com.qure.splash"
    buildFeatures {
        dataBinding = true
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
