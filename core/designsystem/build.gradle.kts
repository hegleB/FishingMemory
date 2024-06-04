import com.fishingmemory.app.setNamespace

plugins {
    id("fishingmemory.android.library")
    id("fishingmemory.android.compose")
}

android {
    setNamespace("core.designsystem")
    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(project(":core:model"))

    implementation(libs.navermap.clustering)
    implementation(libs.compose.glide)
}