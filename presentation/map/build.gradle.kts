plugins {
    id("com.qure.feature")
}

android {
    namespace = "com.qure.map"
    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(project(":core-design"))
    implementation(project(":core"))
    implementation(project(":navigator"))
    implementation(project(":domain"))
    implementation(project(":presentation:memo"))
    implementation(project(":presentation:fishingspot"))

    api(libs.location)
    api(libs.glide.core)
}
