import com.fishingmemory.app.setNamespace

plugins {
    id("fishingmemory.android.feature")
}

android {
    setNamespace("feature.fishingspot")
}

dependencies {
    implementation(project(":feature:memo"))
    implementation(libs.navermap.clustering)
}