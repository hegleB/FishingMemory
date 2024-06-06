import com.fishingmemory.app.setNamespace

plugins {
    id("fishingmemory.android.library")
    id("fishingmemory.android.compose")
    alias(libs.plugins.kotlin.serialization)
}

android {
    setNamespace("core.navigation")
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:ui"))
    implementation(libs.kotlinx.serialization.json)
}