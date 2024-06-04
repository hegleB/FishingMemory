import com.fishingmemory.app.setNamespace

plugins {
    id("fishingmemory.android.library")
    id("kotlinx-serialization")
}

android {
    setNamespace("core.model")
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
}