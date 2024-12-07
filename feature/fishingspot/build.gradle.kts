import com.fishingmemory.app.setNamespace

plugins {
    id("fishingmemory.android.feature")
    id("kotlinx-serialization")
}

android {
    setNamespace("feature.fishingspot")
}

dependencies {
    implementation(project(":feature:memo"))
    implementation(libs.navermap.clustering)
    implementation(libs.kotlinx.serialization.json)
}