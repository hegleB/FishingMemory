import com.fishingmemory.app.setNamespace

plugins {
    id("fishingmemory.android.feature")
}

android {
    setNamespace("feature.memo")
}

dependencies {

    implementation(libs.kakao.share)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.link.ktx)
}