import com.fishingmemory.app.setNamespace

plugins {
    id("fishingmemory.android.feature")
}

android {
    setNamespace("feature.camera")
}

dependencies {
    implementation(libs.mlkit.`object`.detection)
    implementation(libs.camera.camera2)
    implementation(libs.camera.core)
    implementation(libs.camera.view)
    implementation(libs.camera.lifecycle)
}