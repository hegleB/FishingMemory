plugins {
    id("com.qure.library")
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.qure.core_design"
}

dependencies {
    api(libs.chart)
    api(libs.jetpack.ktx)
    api(libs.jetpack.recyclerView)
    api(libs.jetpack.material)
    kapt(libs.hilt.compiler)
}
