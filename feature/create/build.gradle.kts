import com.fishingmemory.app.setNamespace

plugins {
    id("fishingmemory.android.feature")
}

android {
    setNamespace("feature.create")
}

dependencies {
    implementation(project(":build-property"))
    implementation(project(":feature:memo"))
    implementation(libs.play.services.location)
}