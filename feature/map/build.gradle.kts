import com.fishingmemory.app.setNamespace

plugins {
    id("fishingmemory.android.feature")
}

android {
    setNamespace("feature.map")
}

dependencies {
    implementation(libs.play.services.location)
    implementation(libs.navermap.clustering)
}