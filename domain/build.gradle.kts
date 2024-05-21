plugins {
    alias(libs.plugins.org.jetbrains.kotlin.jvm)
}

dependencies {
    api(libs.inject)
    api(libs.kotlin.coroutines)
    api(libs.kotlin.stdilib)
    api(libs.kotlin.coroutinesJvm)
    api(libs.kotlin.coroutinesAndroid)
    implementation(libs.network.kotlin.serialization)
}
