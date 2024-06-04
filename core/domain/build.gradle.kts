import com.fishingmemory.app.setNamespace

plugins {
    id("fishingmemory.android.library")
}

android {
    setNamespace("core.domain")
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:model"))
}