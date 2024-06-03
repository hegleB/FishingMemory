plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.compiler.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidHilt") {
            id = "fishingmemory.android.hilt"
            implementationClass = "com.fishingmemory.app.HiltAndroidPlugin"
        }
        register("kotlinHilt") {
            id = "fishingmemory.kotlin.hilt"
            implementationClass = "com.fishingmemory.app.HiltKotlinPlugin"
        }
    }
}