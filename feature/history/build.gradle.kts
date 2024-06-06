import com.fishingmemory.app.setNamespace

plugins {
    id("fishingmemory.android.feature")
}

android {
    setNamespace("feature.history")
}

dependencies {
    implementation(libs.calendar)
}