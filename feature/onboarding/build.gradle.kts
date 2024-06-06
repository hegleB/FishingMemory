import com.fishingmemory.app.setNamespace

plugins {
    id("fishingmemory.android.feature")
}

android {
    setNamespace("feature.onboarding")
}

dependencies {
    implementation(project(":feature:permission"))
}