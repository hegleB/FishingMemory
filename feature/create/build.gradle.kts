import com.fishingmemory.app.setNamespace

plugins {
    id("fishingmemory.android.feature")
    id("kotlinx-serialization")
}

android {
    setNamespace("feature.create")
}

dependencies {
    implementation(project(":build-property"))
    implementation(project(":feature:memo"))
    implementation(libs.play.services.location)
    implementation(libs.kotlinx.serialization.json)
}