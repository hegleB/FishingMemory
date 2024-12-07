import com.fishingmemory.app.setNamespace

plugins {
    id("fishingmemory.android.feature")
}

android {
    setNamespace("feature.gallery")
}

dependencies {
    implementation(libs.accompanist.permissions)
    implementation(libs.kotlinx.serialization.json)
}