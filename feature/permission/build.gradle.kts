import com.fishingmemory.app.setNamespace

plugins {
    id("fishingmemory.android.feature")
}

android {
    setNamespace("feature.permission")
}

dependencies {
    implementation(project(":feature:login"))
}