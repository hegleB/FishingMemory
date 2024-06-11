import com.fishingmemory.app.setNamespace

plugins {
    id("fishingmemory.android.feature")
    id("kotlinx-serialization")
}

android {
   setNamespace("feature.main")
}

dependencies {
    implementation(project(":feature:create"))
    implementation(project(":feature:fishingspot"))
    implementation(project(":feature:gallery"))
    implementation(project(":feature:history"))
    implementation(project(":feature:home"))
    implementation(project(":feature:login"))
    implementation(project(":feature:map"))
    implementation(project(":feature:memo"))
    implementation(project(":feature:mypage"))
    implementation(project(":feature:onboarding"))
    implementation(project(":feature:permission"))
    implementation(project(":feature:program-information"))
    implementation(project(":feature:splash"))
    implementation(project(":feature:camera"))

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.accompanist.permissions)
}