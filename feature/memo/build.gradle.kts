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
    implementation(libs.firebase.link.ktx)
    implementation(libs.kotlinx.serialization.json)
}