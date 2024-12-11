import com.fishingmemory.app.setNamespace

plugins {
    id("fishingmemory.android.library")
    id("fishingmemory.android.compose")
    id("kotlinx-serialization")
}

android {
    setNamespace("core.ui")
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:designsystem"))

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.navermap.sdk)
    implementation(libs.navermap.clustering)
    implementation(libs.calendar)
    implementation(libs.jetpack.material)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
}