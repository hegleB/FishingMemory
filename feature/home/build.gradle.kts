import com.fishingmemory.app.setNamespace

plugins {
    id("fishingmemory.android.feature")
}

android {
    setNamespace("feature.home")
}

dependencies {
    implementation(libs.play.services.location)
}