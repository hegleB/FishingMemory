import com.fishingmemory.app.setNamespace

plugins {
    id("fishingmemory.android.feature")
}

android {
    setNamespace("feature.login")
}

dependencies {
    implementation(libs.play.services.location)

    implementation(libs.kakao.user)
    implementation(libs.kakao.share)
    implementation(platform(libs.firebase.bom))
}