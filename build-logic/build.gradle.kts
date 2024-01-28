import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidLibrary") {
            id = "com.qure.library"
            implementationClass = "gradle.plugin.AndroidLibraryPlugin"
        }
        register("androidHilt") {
            id = "com.qure.hilt"
            implementationClass = "gradle.plugin.AndroidHiltPlugin"
        }
    }
}
