import com.fishingmemory.app.setNamespace

plugins {
    id("fishingmemory.android.library")
    id("fishingmemory.android.room")
    id("fishingmemory.android.hilt")
    id("kotlinx-serialization")
}

android {
    setNamespace("core.data")
}

dependencies {
    implementation(project(":build-property"))
    implementation(project(":core:model"))

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.datastore)
}