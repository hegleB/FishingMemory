import com.fishingmemory.app.setNamespace

plugins {
    id("fishingmemory.android.feature")
    id("kotlinx-serialization")
}

android {
    setNamespace("feature.memo")
}

dependencies {

    implementation(libs.kakao.share)
    implementation(platform(libs.firebase.bom))
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.io.branch.sdk)
    implementation(libs.play.services.ads.identifier)
    implementation(libs.androidx.browser)
}